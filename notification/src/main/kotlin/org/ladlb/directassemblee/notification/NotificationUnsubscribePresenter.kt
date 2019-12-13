package org.ladlb.directassemblee.notification

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import org.ladlb.directassemblee.analytics.AnalyticsHelper
import org.ladlb.directassemblee.core.AbstractViewModel
import org.ladlb.directassemblee.notification.NotificationUnsubscribePresenter.Unsubscribe.UnsubscribeComplete
import org.ladlb.directassemblee.notification.NotificationUnsubscribePresenter.Unsubscribe.UnsubscribeError

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

class NotificationUnsubscribePresenter(private val repository: NotificationRepository, private val preferences: NotificationStorage?, private val analyticsHelper: AnalyticsHelper?) : AbstractViewModel() {

    sealed class Unsubscribe {
        object UnsubscribeComplete : Unsubscribe()
        object UnsubscribeError : Unsubscribe()
    }

    private val _viewState = MutableLiveData<Unsubscribe>()
    val viewState: LiveData<Unsubscribe> = _viewState

    fun postUnSubscribe(id: String, token: String?, deputyId: Int) {

        if (TextUtils.isEmpty(token)) {
            analyticsHelper?.track("Token empty in unsubscribe")
            _viewState.postValue(UnsubscribeError)
        } else {

            launch {
                try {
                    repository.postUnSubscribe(id, token!!, deputyId)
                    preferences?.setNotificationEnabled(false)
                    _viewState.postValue(UnsubscribeComplete)
                } catch (e: Throwable) {
                    _viewState.postValue(UnsubscribeError)
                }
            }

        }

    }

}
