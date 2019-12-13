package org.ladlb.directassemblee.notification.firebase

import androidx.annotation.CallSuper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject
import org.ladlb.directassemblee.notification.NotificationStorage

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

abstract class FirebaseMessagingService : FirebaseMessagingService() {

    private val notificationStorage: NotificationStorage by inject()

    @CallSuper
    override fun onMessageReceived(p0: RemoteMessage) {
        if (notificationStorage.isNotificationEnabled()) {
            super.onMessageReceived(p0)
        }
    }

    @CallSuper
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        notificationStorage.saveFirebaseToken(token)
    }

}
