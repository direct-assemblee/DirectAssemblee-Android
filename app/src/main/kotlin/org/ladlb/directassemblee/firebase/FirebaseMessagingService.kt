package org.ladlb.directassemblee.firebase

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import org.ladlb.directassemblee.api.ladlb.RetrofitApiRepository
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter.NotificationSubscribeView
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

class FirebaseMessagingService : FirebaseMessagingService(), NotificationSubscribeView {

    @Inject
    lateinit var retrofitApiRepository: RetrofitApiRepository

    @Inject
    lateinit var subscribeNotificationPresenter: NotificationSubscribePresenter

    @Inject
    lateinit var preferenceStorage: PreferencesStorageImpl

    override fun onCreate() {
        super.onCreate()

        AndroidInjection.inject(this)
    }

    override fun onDestroy() {
        subscribeNotificationPresenter.onDestroy()
        super.onDestroy()
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        if (preferenceStorage.isNotificationEnabled()) {
            super.onMessageReceived(p0)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        preferenceStorage.saveFirebaseToken(token)

        val deputy = preferenceStorage.loadDeputy()

        if (preferenceStorage.isNotificationEnabled() && deputy != null) {
            subscribeNotificationPresenter.postSubscribe(
                    retrofitApiRepository,
                    FirebaseInstanceId.getInstance().id,
                    token,
                    deputy.id,
                    preferenceStorage
            )
        }
    }

    override fun onNotificationSubscribeCompleted() {
        // Nothing to do
    }

    override fun onNotificationSubscribeFailed() {
        // Nothing to do
    }

}
