package org.ladlb.directassemblee.firebase

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.google.firebase.iid.FirebaseInstanceIdService
import org.ladlb.directassemblee.AssembleApplication
import org.ladlb.directassemblee.api.ladlb.RetrofitApiRepository
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter
import org.ladlb.directassemblee.preferences.PreferencesStorage

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

class FireBaseInstanceIdService : FirebaseInstanceIdService(), LifecycleOwner, NotificationSubscribePresenter.NotificationSubscribeView {

    private var mSubscribeNotificationPresenter: NotificationSubscribePresenter? = null

    private lateinit var lifecycleRegistry: LifecycleRegistry

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    override fun onCreate() {
        super.onCreate()

        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)

        mSubscribeNotificationPresenter = NotificationSubscribePresenter(
                this,
                lifecycle
        )

    }

    override fun onDestroy() {
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
        super.onDestroy()
    }

    override fun onTokenRefresh() {

        val deputy = getPreferences().loadDeputy()

        if (getPreferences().isNotificationEnabled() && deputy != null) {
            mSubscribeNotificationPresenter!!.postSubscribe(
                    getApiServices(),
                    getPreferences(),
                    deputy.id
            )
        }

    }

    override fun onNotificationSubscribeCompleted() {

    }

    override fun onNotificationSubscribeFailed() {

    }

    fun getPreferences(): PreferencesStorage = (application as AssembleApplication).getPreferences()

    fun getApiServices(): RetrofitApiRepository =
            (application as AssembleApplication).getApiServices()

}
