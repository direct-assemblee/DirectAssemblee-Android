package org.ladlb.directassemblee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.core.AbstractToolBarActivity
import org.ladlb.directassemblee.core.helper.ErrorHelper
import org.ladlb.directassemblee.core.helper.NavigationHelper
import org.ladlb.directassemblee.core.helper.collect
import org.ladlb.directassemblee.deputy.DeputyTimelineFragment
import org.ladlb.directassemblee.deputy.retrieve.DeputyRetrieveActivity
import org.ladlb.directassemblee.deputy.search.DeputySearchActivity
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.notification.NotificationStorage
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter.Subscribe.SubscribeComplete
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter.Subscribe.SubscribeError
import org.ladlb.directassemblee.settings.SettingsActivity
import org.ladlb.directassemblee.synthesis.SynthesisActivity
import org.ladlb.directassemblee.timeline.TimelineFragment.DeputyTimeLineFragmentListener

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

class DashboardActivity : AbstractToolBarActivity(), DeputyTimeLineFragmentListener, OnNavigationItemSelectedListener {

    companion object Factory {

        private const val REQUEST_SETTINGS: Int = 1

        var EXTRA_DEPUTY: String = "EXTRA_DEPUTY"

        fun getIntent(context: Context, deputy: Deputy): Intent {

            val intent = Intent(context, DashboardActivity::class.java)
            intent.putExtra(EXTRA_DEPUTY, deputy)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            return intent

        }

    }

    private lateinit var deputy: Deputy

    private val subscribeNotificationPresenter: NotificationSubscribePresenter by viewModel()

    private val notificationStorage: NotificationStorage by inject()

    private val preferenceStorage: org.ladlb.directassemblee.storage.PreferencesStorage by inject()

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deputy = intent.getParcelableExtra(EXTRA_DEPUTY)!!

        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(
                R.id.frameLayout,
                DeputyTimelineFragment.newInstance(deputy),
                DeputyTimelineFragment.TAG
        ).commit()

        subscribeNotificationPresenter.viewState.collect(this) {
            when (it) {
                is SubscribeComplete -> onNotificationSubscribeCompleted()
                is SubscribeError -> onNotificationSubscribeFailed()
            }
        }

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        showNotificationDialogIfNeeded()
        updateNavigationDrawerEntries()
    }

    private fun updateNavigationDrawerEntries() {
        navigationView.menu.findItem(R.id.nav_deputy).isChecked = true
    }

    override fun getContentView(): Int = R.layout.activity_dashboard

    private fun startSettingsActivity() {
        startActivityForResult(
                SettingsActivity.getIntent(this, deputy),
                REQUEST_SETTINGS
        )
    }

    private fun startSynthesisActivity() {
        startActivity(SynthesisActivity.getIntent(this))
    }

    private fun showNotificationDialogIfNeeded() {

        if (!preferenceStorage.isNotificationDialogShowed()) {

            preferenceStorage.setNotificationDialogShowed(true)

            AlertDialog.Builder(
                    this
            ).setMessage(
                    R.string.onboarding_enable_notifications
            ).setPositiveButton(
                    R.string.vote_result_for
            ) { _, _ ->
                subscribeNotificationPresenter.postSubscribe(
                        FirebaseInstanceId.getInstance().id,
                        notificationStorage.getFirebaseToken(),
                        deputy.id
                )
            }.setNegativeButton(
                    R.string.vote_result_against
            ) { _, _ ->
                tagNotificationsState(false)
            }.create().show()

        }

    }

    private fun onNotificationSubscribeCompleted() {
        tagNotificationsState(true)
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
                FirebaseAnalyticsKeys.Event.NOTIFICATIONS_ENABLE,
                bundle
        )

    }

    private fun onNotificationSubscribeFailed() {
        ErrorHelper.showErrorAlertDialog(this, R.string.notification_update_error)
    }

    private fun startDeputySearchActivity() {
        startActivity(
                DeputySearchActivity.getIntent(this)
        )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_search -> startDeputySearchActivity()
            R.id.nav_share -> {

                firebaseAnalyticsManager.logEvent(
                        FirebaseAnalyticsKeys.Event.SHARE,
                        Bundle()
                )

                NavigationHelper.share(
                        this,
                        String.format(
                                ("%s\n\n%s"),
                                getString(
                                        R.string.share_default_text,
                                        deputy.activityRate
                                ),
                                getString(R.string.url_website)
                        )
                )

            }
            R.id.nav_settings -> {
                startSettingsActivity()
            }
            R.id.nav_donation -> {
                NavigationHelper.openURL(
                        this,
                        getString(R.string.url_tipeee)
                )
            }
            R.id.nav_synthesis -> {
                startSynthesisActivity()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return false
    }

    override fun onRefreshTimeLine(deputy: Deputy) {
        val fragment = supportFragmentManager.findFragmentByTag(DeputyTimelineFragment.TAG)
        if (fragment is DeputyTimelineFragment) {
            fragment.setDeputy(deputy)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_SETTINGS -> {
                startActivity(DeputyRetrieveActivity.getIntent(this))
                finish()
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
