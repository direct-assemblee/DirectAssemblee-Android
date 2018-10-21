package org.ladlb.directassemblee.timeline

import androidx.lifecycle.Lifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.ladlb.directassemblee.AbstractPresenter
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.timeline.TimelineGetPresenter.TimelineGetView

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

class TimelineGetPresenter(view: TimelineGetView, lifecycle: Lifecycle?) : AbstractPresenter<TimelineGetView>(view, lifecycle) {

    fun getTimeline(apiRepository: ApiRepository, deputyId: Int, page: Int) {

        apiRepository.getTimeline(
                deputyId, page
        ).subscribeOn(
                Schedulers.io()
        ).observeOn(
                AndroidSchedulers.mainThread()
        ).doOnSubscribe {
            t: Disposable -> call(t)
        }.subscribe(
                Consumer { timelineItems -> view?.onTimelineReceived(timelineItems) },
                object : AbstractPresenter.AbstractErrorConsumer() {
                    override fun onError(t: Throwable) {
                        view?.onGetTimelineRequestFailed()
                    }
                }
        )

    }

    interface TimelineGetView : BaseView {

        fun onTimelineReceived(timelineItem: Array<TimelineItem>)

        fun onGetTimelineRequestFailed()

    }

}
