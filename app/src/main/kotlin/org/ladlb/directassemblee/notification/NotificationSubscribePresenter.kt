package org.ladlb.directassemblee.notification

import android.text.TextUtils
import kotlinx.coroutines.launch
import org.ladlb.directassemblee.AbstractPresenter
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.helper.MetricHelper
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter.NotificationSubscribeView
import org.ladlb.directassemblee.preferences.PreferencesStorage
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

class NotificationSubscribePresenter @Inject
constructor(val apiRepository: ApiRepository, view: NotificationSubscribeView) : AbstractPresenter<NotificationSubscribeView>(view) {

    fun postSubscribe(id: String, token: String?, deputyId: Int, preferences: PreferencesStorage?) {

        if (TextUtils.isEmpty(token)) {
            MetricHelper.track("Token empty in subscribe")
            preferences?.setNotificationEnabled(false)
            view?.onNotificationSubscribeFailed()
        } else {

            launch {
                try {
                    apiRepository.postSubscribe(id, token!!, deputyId)
                    preferences?.setNotificationEnabled(true)
                    view?.onNotificationSubscribeCompleted()
                } catch (e: Throwable) {
                    preferences?.setNotificationEnabled(false)
                    view?.onNotificationSubscribeFailed()
                }
            }

        }

    }

    interface NotificationSubscribeView : BaseView {

        fun onNotificationSubscribeCompleted()

        fun onNotificationSubscribeFailed()

    }

}
