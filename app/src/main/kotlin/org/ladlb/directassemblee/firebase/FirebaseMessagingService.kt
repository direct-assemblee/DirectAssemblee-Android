package org.ladlb.directassemblee.firebase

import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.ladlb.directassemblee.notification.NotificationRepository
import org.ladlb.directassemblee.notification.NotificationStorage
import org.ladlb.directassemblee.notification.firebase.FirebaseMessagingService

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

class FirebaseMessagingService : FirebaseMessagingService() {

    private val repository: NotificationRepository by inject()

    private val notificationStorage: NotificationStorage by inject()

    private val preferenceStorage: org.ladlb.directassemblee.storage.PreferencesStorage by inject()

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onDestroy() {
        serviceJob.cancel()
        super.onDestroy()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val deputy = preferenceStorage.loadDeputy()

        if (notificationStorage.isNotificationEnabled() && deputy != null) {
            serviceScope.launch {
                repository.postSubscribe(
                        FirebaseInstanceId.getInstance().id,
                        token,
                        deputy.id
                )
            }
        }
    }

}
