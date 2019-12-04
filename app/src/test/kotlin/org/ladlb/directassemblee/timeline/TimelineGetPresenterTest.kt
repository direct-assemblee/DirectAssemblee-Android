package org.ladlb.directassemblee.timeline

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.ladlb.directassemblee.PresenterTest
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.timeline.TimelineGetPresenter.TimelineGetView
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.*

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

class TimelineGetPresenterTest : PresenterTest() {

    private val presenter: TimelineGetPresenter

    private val view = mock(TimelineGetView::class.java)

    private val apiRepository = mock(ApiRepository::class.java)

    private val timelineCacheManager = mock(TimelineCacheManager::class.java)

    init {

        presenter = TimelineGetPresenter(apiRepository, view)
        presenter.context = Dispatchers.Unconfined

    }

    @Test
    fun getTimeline_Success() = runBlocking {

        val result = arrayOf<TimelineItem>()

        `when`(apiRepository.getTimeline(anyInt(), anyInt())).thenReturn(result)
        `when`(timelineCacheManager.get(anyInt(), anyInt())).thenReturn(null)

        presenter.getTimeline(timelineCacheManager, anyInt(), anyInt())

        verify(view, atLeastOnce()).onTimelineReceived(result)

    }

    @Test
    fun getTimeline_Fail() = runBlocking {

        `when`(apiRepository.getTimeline(anyInt(), anyInt())).thenThrow(NullPointerException())
        `when`(timelineCacheManager.get(anyInt(), anyInt())).thenReturn(null)

        presenter.getTimeline(timelineCacheManager, anyInt(), anyInt())

        verify(view, atLeastOnce()).onGetTimelineRequestFailed()

    }

}