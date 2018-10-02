package org.ladlb.directassemblee.notification

import android.text.TextUtils
import androidx.lifecycle.Lifecycle
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
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

class NotificationUnSubscribePresenter(view: NotificationUnSubscribeView, lifecycle: Lifecycle?) : AbstractPresenter<NotificationUnSubscribeView>(view, lifecycle) {

    fun postUnSubscribe(apiRepository: ApiRepository, preferences: PreferencesStorage, deputyId: Int) {

        val fireBaseInstanceId = FirebaseInstanceId.getInstance()

        val id = fireBaseInstanceId.id
        val token = fireBaseInstanceId.token

        if (TextUtils.isEmpty(token)) {
            throw NullPointerException("Token null or empty")
        } else {
            apiRepository.postUnSubscribe(
                    id, token!!, deputyId
            ).subscribeOn(
                    Schedulers.io()
            ).observeOn(
                    AndroidSchedulers.mainThread()
            ).doOnSubscribe(
                    { disposable -> call(disposable) }
            ).subscribe(
                    Action {
                        preferences.setNotificationEnabled(false)
                        view?.onNotificationUnSubscribeCompleted()
                    },
                    object : AbstractPresenter.AbstractErrorConsumer() {
                        override fun onError(t: Throwable) {
                            view?.onNotificationUnSubscribeFailed()
                        }
                    }
            )
        }

    }

    interface NotificationUnSubscribeView : BaseView {

        fun onNotificationUnSubscribeCompleted()

        fun onNotificationUnSubscribeFailed()

    }

}
