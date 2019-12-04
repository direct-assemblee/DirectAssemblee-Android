package org.ladlb.directassemblee.timeline

import kotlinx.coroutines.launch
import org.ladlb.directassemblee.AbstractPresenter
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.timeline.TimelineGetPresenter.TimelineGetView
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

class TimelineGetPresenter @Inject
constructor(val apiRepository: ApiRepository, view: TimelineGetView) : AbstractPresenter<TimelineGetView>(view) {

    fun getTimeline(timelineCacheManager: TimelineCacheManager, deputyId: Int, page: Int) {

        launch {
            try {
                var items = timelineCacheManager.get(deputyId, page)
                if (items == null || items.isEmpty()) {
                    items = apiRepository.getTimeline(deputyId, page)
                    timelineCacheManager.put(deputyId, page, items)
                }
                view?.onTimelineReceived(items)
            } catch (e: Throwable) {
                view?.onGetTimelineRequestFailed()
            }
        }

    }

    interface TimelineGetView : BaseView {

        fun onTimelineReceived(timelineItem: Array<TimelineItem>)

        fun onGetTimelineRequestFailed()

    }

}
