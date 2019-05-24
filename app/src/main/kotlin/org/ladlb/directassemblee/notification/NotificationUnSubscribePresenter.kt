package org.ladlb.directassemblee.notification

import android.text.TextUtils
import kotlinx.coroutines.launch
import org.ladlb.directassemblee.AbstractPresenter
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.notification.NotificationUnSubscribePresenter.NotificationUnSubscribeView
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

class NotificationUnSubscribePresenter(view: NotificationUnSubscribeView) : AbstractPresenter<NotificationUnSubscribeView>(view) {

    fun postUnSubscribe(apiRepository: ApiRepository, id: String, token: String?, deputyId: Int, preferences: PreferencesStorage?) {

        if (TextUtils.isEmpty(token)) {
            throw NullPointerException("Token null or empty")
        } else {

            launch {
                try {
                    apiRepository.postUnSubscribe(id, token!!, deputyId)
                    preferences?.setNotificationEnabled(false)
                    view?.onNotificationUnSubscribeCompleted()
                } catch (e: Throwable) {
                    view?.onNotificationUnSubscribeFailed()
                }
            }

        }

    }

    interface NotificationUnSubscribeView : BaseView {

        fun onNotificationUnSubscribeCompleted()

        fun onNotificationUnSubscribeFailed()

    }

}
