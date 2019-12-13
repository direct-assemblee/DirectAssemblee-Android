package org.ladlb.directassemblee.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import org.ladlb.directassemblee.core.AbstractViewModel
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter.Subscribe.SubscribeComplete
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter.Subscribe.SubscribeError

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

class NotificationSubscribePresenter(private val repository: NotificationRepository) : AbstractViewModel() {

    sealed class Subscribe {
        object SubscribeComplete : Subscribe()
        object SubscribeError : Subscribe()
    }

    private val _viewState = MutableLiveData<Subscribe>()
    val viewState: LiveData<Subscribe> = _viewState

    fun postSubscribe(id: String, token: String?, deputyId: Int) {

        launch {
            try {
                repository.postSubscribe(id, token!!, deputyId)
                _viewState.postValue(SubscribeComplete)
            } catch (e: Throwable) {
                _viewState.postValue(SubscribeError)
            }
        }

    }

}
