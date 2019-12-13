package org.ladlb.directassemblee.settings

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.SwitchPreferenceCompat
import com.google.firebase.iid.FirebaseInstanceId
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.ladlb.directassemblee.analytics.AnalyticsHelper
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.ItemKey
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.UserProperty.Companion.DISTRICT
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.UserProperty.Companion.PARLIAMENT_GROUP
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.core.AbstractPreferenceFragment
import org.ladlb.directassemblee.core.helper.ErrorHelper
import org.ladlb.directassemblee.core.helper.NavigationHelper
import org.ladlb.directassemblee.core.helper.collect
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.notification.NotificationStorage
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter.Subscribe.SubscribeComplete
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter.Subscribe.SubscribeError
import org.ladlb.directassemblee.notification.NotificationUnsubscribePresenter
import org.ladlb.directassemblee.notification.NotificationUnsubscribePresenter.Unsubscribe.UnsubscribeComplete
import org.ladlb.directassemblee.notification.NotificationUnsubscribePresenter.Unsubscribe.UnsubscribeError
import org.ladlb.directassemblee.storage.PreferencesStorage

/**
 * This file is part of DirectAssemblee-Android <https://github.com/direct-assemblee/DirectAssemblee-Android>.
 *
 * DirectAssemblee-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DirectAssemblee-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DirectAssemblee-Android. If not, see <http://www.gnu.org/licenses/>.
 */

class SettingsFragment : AbstractPreferenceFragment(), OnPreferenceChangeListener {

    override fun getClassName(): String = "SettingsFragment"

    companion object {

        var TAG: String = SettingsFragment::class.java.name

        private const val ARG_DEPUTY: String = "ARG_DEPUTY"

        fun newInstance(deputy: Deputy): SettingsFragment {

            val bundle = Bundle()
            bundle.putParcelable(ARG_DEPUTY, deputy)

            val fragment = SettingsFragment()
            fragment.arguments = bundle

            return fragment

        }

    }

    private val subscribeNotificationPresenter: NotificationSubscribePresenter by viewModel()

    private val unSubscribeNotificationPresenter: NotificationUnsubscribePresenter by viewModel()

    private val preferenceStorage: PreferencesStorage by inject()

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by inject()

    private val analyticsHelper: AnalyticsHelper by inject()

    private val notificationStorage: NotificationStorage by inject()

    private var isChangeDeputy = false

    private lateinit var notificationSwitchPreference: SwitchPreferenceCompat

    private lateinit var deputy: Deputy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deputy = arguments!!.getParcelable(ARG_DEPUTY)!!

        subscribeNotificationPresenter.viewState.collect(this) {
            when (it) {
                is SubscribeComplete -> onNotificationSubscribeCompleted()
                is SubscribeError -> onNotificationSubscribeFailed()
            }
        }

        unSubscribeNotificationPresenter.viewState.collect(this) {
            when (it) {
                is UnsubscribeComplete -> onNotificationUnSubscribeCompleted()
                is UnsubscribeError -> onNotificationUnSubscribeFailed()
            }
        }
    }

    private var listener: SettingsFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = activity as SettingsFragmentListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement SettingsFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationSwitchPreference = findPreference(getString(R.string.preferences_settings_key_notifications)) as SwitchPreferenceCompat

        if (TextUtils.isEmpty(notificationStorage.getFirebaseToken())) {
            analyticsHelper.track("Token empty in settings")
            notificationSwitchPreference.isEnabled = false
        } else {
            notificationSwitchPreference.isChecked = notificationStorage.isNotificationEnabled()
            notificationSwitchPreference.onPreferenceChangeListener = this
        }

        findPreference(getString(R.string.preferences_settings_key_deputy_change)).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    showSearchDialog()
                    return@OnPreferenceClickListener true
                }

        findPreference(getString(R.string.preferences_settings_key_faq)).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    NavigationHelper.openURL(
                            context!!,
                            getString(R.string.url_faq)
                    )
                    return@OnPreferenceClickListener true
                }

        findPreference(getString(R.string.preferences_settings_key_policy)).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    NavigationHelper.openURL(
                            context!!,
                            getString(R.string.url_policy)
                    )
                    return@OnPreferenceClickListener true
                }

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_settings)
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        if (newValue is Boolean) {
            onNotificationsStateChange(newValue)
            return true
        }
        return false
    }

    private fun showSearchDialog() {

        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(R.string.home_unfollow_deputy_confirmation)
        builder.setPositiveButton(
                R.string.ok
        ) { _, _ ->
            firebaseAnalyticsManager.logEvent(
                    Event.CONFIRM_CHANGE_DEPUTY,
                    FirebaseAnalyticsHelper.addDeputy(
                            Bundle(),
                            deputy
                    )
            )
            unsubscribeIfNeeded()
        }
        builder.setNegativeButton(
                R.string.cancel
        ) { _, _ ->
            firebaseAnalyticsManager.logEvent(
                    Event.DENY_CHANGE_DEPUTY,
                    FirebaseAnalyticsHelper.addDeputy(
                            Bundle(),
                            deputy
                    )
            )
        }
        builder.create().show()

    }

    private fun unsubscribeIfNeeded() {

        if (notificationStorage.isNotificationEnabled()) {

            isChangeDeputy = true
            unSubscribeNotificationPresenter.postUnSubscribe(
                    FirebaseInstanceId.getInstance().id,
                    notificationStorage.getFirebaseToken(),
                    deputy.id
            )

            listener?.showLoadingView(getString(R.string.notification_popup_unsubscribe))

        } else {
            clearUserProperty()
            startRetrieveDeputyActivity()
        }

    }

    private fun clearUserProperty() {
        preferenceStorage.saveDeputy(null)
        preferenceStorage.setNotificationDialogShowed(false)
        firebaseAnalyticsManager.clearUserProperty(PARLIAMENT_GROUP, DISTRICT)
    }

    private fun startRetrieveDeputyActivity() {
        listener?.startRetrieveDeputyActivity()
    }

    private fun onNotificationsStateChange(enable: Boolean) {

        notificationSwitchPreference.isEnabled = false

        if (enable) {
            subscribeNotificationPresenter.postSubscribe(
                    FirebaseInstanceId.getInstance().id,
                    notificationStorage.getFirebaseToken(),
                    deputy.id
            )
        } else {
            unSubscribeNotificationPresenter.postUnSubscribe(
                    FirebaseInstanceId.getInstance().id,
                    notificationStorage.getFirebaseToken(),
                    deputy.id
            )
        }

    }

    private fun onNotificationSubscribeCompleted() {
        tagNotificationsState(true)
        notificationSwitchPreference.isEnabled = true
    }

    private fun onNotificationSubscribeFailed() {
        notificationSwitchPreference.onPreferenceChangeListener = null
        notificationSwitchPreference.isChecked = false
        notificationSwitchPreference.onPreferenceChangeListener = this
        notificationSwitchPreference.isEnabled = true
        ErrorHelper.showErrorAlertDialog(context!!, R.string.notification_update_error)
    }

    private fun onNotificationUnSubscribeCompleted() {

        tagNotificationsState(false)

        if (isChangeDeputy) {
            isChangeDeputy = false
            clearUserProperty()
            startRetrieveDeputyActivity()
        } else {
            notificationSwitchPreference.isEnabled = true
        }

    }

    private fun onNotificationUnSubscribeFailed() {
        if (isChangeDeputy) {
            isChangeDeputy = false
            listener?.hideLoadingView()
            ErrorHelper.showErrorAlertDialog(context!!, R.string.generic_error)
        } else {
            notificationSwitchPreference.onPreferenceChangeListener = null
            notificationSwitchPreference.isChecked = true
            notificationSwitchPreference.onPreferenceChangeListener = this
            notificationSwitchPreference.isEnabled = true
            ErrorHelper.showErrorAlertDialog(context!!, R.string.notification_update_error)
        }
    }

    private fun tagNotificationsState(enable: Boolean) {

        val bundle = FirebaseAnalyticsHelper.addDeputy(
                Bundle(),
                deputy
        )
        bundle.putBoolean(
                ItemKey.ENABLE,
                enable
        )
        firebaseAnalyticsManager.logEvent(
                Event.NOTIFICATIONS_ENABLE,
                bundle
        )

    }

    interface SettingsFragmentListener {
        fun showLoadingView(label: CharSequence)

        fun hideLoadingView()

        fun startRetrieveDeputyActivity()
    }

}
