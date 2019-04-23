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
import dagger.android.support.AndroidSupportInjection
import org.ladlb.directassemblee.AbstractPreferenceFragment
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.api.ladlb.RetrofitApiRepository
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.deputy.retrieve.DeputyRetrieveActivity
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.helper.ErrorHelper
import org.ladlb.directassemblee.helper.MetricHelper
import org.ladlb.directassemblee.helper.NavigationHelper
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter.NotificationSubscribeView
import org.ladlb.directassemblee.notification.NotificationUnSubscribePresenter
import org.ladlb.directassemblee.notification.NotificationUnSubscribePresenter.NotificationUnSubscribeView
import org.ladlb.directassemblee.preferences.PreferencesStorageImpl
import javax.inject.Inject

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

class SettingsFragment : AbstractPreferenceFragment(), NotificationSubscribeView, NotificationUnSubscribeView, OnPreferenceChangeListener {

    override fun getClassName(): String = "SettingsFragment"

    companion object {

        var TAG: String = SettingsFragment::class.java.name

        fun newInstance(): SettingsFragment {

            val bundle = Bundle()

            val fragment = SettingsFragment()
            fragment.arguments = bundle

            return fragment

        }

    }

    @Inject
    lateinit var subscribeNotificationPresenter: NotificationSubscribePresenter

    @Inject
    lateinit var unSubscribeNotificationPresenter: NotificationUnSubscribePresenter

    @Inject
    lateinit var apiRepository: RetrofitApiRepository

    @Inject
    lateinit var preferenceStorage: PreferencesStorageImpl

    private var isChangeDeputy = false

    private lateinit var notificationSwitchPreference: SwitchPreferenceCompat

    private lateinit var deputy: Deputy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidSupportInjection.inject(this)

        deputy = preferenceStorage.loadDeputy()!!
    }

    private var listener: SettingsFragmentListener? = null

    override fun onAttach(context: Context?) {
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

        val fireBaseInstanceId = FirebaseInstanceId.getInstance()

        if (TextUtils.isEmpty(fireBaseInstanceId.token)) {
            MetricHelper.track("Token empty in settings")
            notificationSwitchPreference.isEnabled = false
        } else {
            notificationSwitchPreference.isChecked = preferenceStorage.isNotificationEnabled()
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
            startClearDeputy()
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

    private fun startClearDeputy() {

        val preferences = preferenceStorage
        if (preferences.isNotificationEnabled()) {
            val deputyId = preferences.loadDeputy()!!.id

            isChangeDeputy = true
            unSubscribeNotificationPresenter.postUnSubscribe(
                    apiRepository,
                    preferenceStorage,
                    deputyId
            )

            listener?.showLoadingView(getString(R.string.notification_popup_unsubscribe))

        } else {
            finishClearDeputy()
        }

    }

    private fun finishClearDeputy() {
        val preferences = preferenceStorage
        preferences.saveDeputy(null)
        preferences.setNotificationDialogShowed(false)
        firebaseAnalyticsManager.clearUserDeputyProperties()
        startRetrieveDeputyActivity()
    }

    private fun startRetrieveDeputyActivity() {
        startActivity(DeputyRetrieveActivity.getIntent(context!!))
    }

    private fun onNotificationsStateChange(enable: Boolean) {

        notificationSwitchPreference.isEnabled = false

        val preferences = preferenceStorage
        val deputyId = preferences.loadDeputy()!!.id

        if (enable) {
            subscribeNotificationPresenter.postSubscribe(
                    apiRepository,
                    preferenceStorage,
                    deputyId
            )
        } else {
            unSubscribeNotificationPresenter.postUnSubscribe(
                    apiRepository,
                    preferenceStorage,
                    deputyId
            )
        }

    }

    override fun onNotificationSubscribeCompleted() {
        tagNotificationsState(true)
        notificationSwitchPreference.isEnabled = true
    }

    override fun onNotificationSubscribeFailed() {
        notificationSwitchPreference.onPreferenceChangeListener = null
        notificationSwitchPreference.isChecked = false
        notificationSwitchPreference.onPreferenceChangeListener = this
        notificationSwitchPreference.isEnabled = true
        ErrorHelper.showErrorAlertDialog(context!!, R.string.notification_update_error)
    }

    override fun onNotificationUnSubscribeCompleted() {

        tagNotificationsState(false)

        if (isChangeDeputy) {
            isChangeDeputy = false
            finishClearDeputy()
        } else {
            notificationSwitchPreference.isEnabled = true
        }

    }

    override fun onNotificationUnSubscribeFailed() {
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
                FirebaseAnalyticsKeys.ItemKey.ENABLE,
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
    }

}
