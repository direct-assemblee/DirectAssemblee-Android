package org.ladlb.directassemblee.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import org.ladlb.directassemblee.core.AbstractViewModel
import org.ladlb.directassemblee.model.TimelineItem
import org.ladlb.directassemblee.timeline.TimelineGetPresenter.TimelineGet.*

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

class TimelineGetPresenter(private val timelineRepository: TimelineRepository, private val deputyId: Int) : AbstractViewModel() {

    sealed class TimelineGet {
        class Init(val items: List<TimelineItem>) : TimelineGet()
        class Add(val items: List<TimelineItem>) : TimelineGet()
        object Error : TimelineGet()
    }

    private val _viewState = MutableLiveData<TimelineGet>()
    val viewState: LiveData<TimelineGet> = _viewState

    fun get() {
        launch {
            try {
                _viewState.postValue(Init(timelineRepository.get(deputyId)))
            } catch (e: Exception) {
                _viewState.postValue(Error)
            }
        }
    }

    fun loadMore() {
        launch {
            try {
                _viewState.postValue(Add(timelineRepository.loadMore(deputyId)))
            } catch (e: Exception) {
                _viewState.postValue(Error)
            }
        }
    }


    fun reload() {
        launch {
            try {
                timelineRepository.clear(deputyId)
                _viewState.postValue(Init(timelineRepository.get(deputyId)))
            } catch (e: Exception) {
                _viewState.postValue(Error)
            }
        }
    }

}
