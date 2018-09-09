package org.ladlb.directassemblee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.deputy.DeputyFragment
import org.ladlb.directassemblee.deputy.search.DeputySearchActivity
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.ItemKey
import org.ladlb.directassemblee.helper.ErrorHelper
import org.ladlb.directassemblee.helper.NavigationHelper
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter.NotificationSubscribeView
import org.ladlb.directassemblee.settings.SettingsActivity
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

class DashboardActivity : AbstractToolBarActivity(), NotificationSubscribeView, DeputyTimeLineFragmentListener, OnNavigationItemSelectedListener {

    companion object Factory {

        var EXTRA_DEPUTY: String = "EXTRA_DEPUTY"

        fun getIntent(context: Context, deputy: Deputy): Intent {

            val intent = Intent(context, DashboardActivity::class.java)
            intent.putExtra(EXTRA_DEPUTY, deputy)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            return intent

        }

    }

    private lateinit var deputy: Deputy

    private lateinit var subscribeNotificationPresenter: NotificationSubscribePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeNotificationPresenter = NotificationSubscribePresenter(
                this,
                lifecycle
        )

        deputy = intent.getParcelableExtra(EXTRA_DEPUTY)

        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(
                R.id.frameLayout,
                DeputyFragment.newInstance(deputy),
                DeputyFragment.TAG
        ).commit()

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
        startActivity(SettingsActivity.getIntent(this))
    }

    private fun showNotificationDialogIfNeeded() {

        if (!getPreferences().isNotificationDialogShowed()) {

            getPreferences().setNotificationDialogShowed(true)

            AlertDialog.Builder(
                    this
            ).setMessage(
                    R.string.onboarding_enable_notifications
            ).setPositiveButton(
                    R.string.vote_result_for
            ) { _, _ ->
                subscribeNotificationPresenter.postSubscribe(
                        getApiServices(),
                        getPreferences(),
                        deputy.id
                )
            }.setNegativeButton(
                    R.string.vote_result_against
            ) { _, _ ->
                tagNotificationsState(false)
            }.create().show()

        }

    }

    override fun onNotificationSubscribeCompleted() {
        tagNotificationsState(true)
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
        getFireBaseAnalytics().logEvent(
                Event.NOTIFICATIONS_ENABLE,
                bundle
        )

    }

    override fun onNotificationSubscribeFailed() {
        ErrorHelper.showErrorAlertDialog(this, R.string.notification_update_error)
    }

    override fun onRefreshTimeLine(deputy: Deputy) {
        val fragment = supportFragmentManager.findFragmentByTag(DeputyFragment.TAG)
        if (fragment is DeputyFragment) {
            fragment.setDeputy(deputy)
        }
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

                getFireBaseAnalytics().logEvent(
                        Event.SHARE,
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
            R.id.nav_faq -> {
                NavigationHelper.openURL(
                        this,
                        getString(R.string.url_faq)
                )
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return false
    }

}