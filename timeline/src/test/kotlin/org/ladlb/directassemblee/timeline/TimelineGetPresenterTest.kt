package org.ladlb.directassemblee.timeline

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.Test
import org.ladlb.directassemblee.timeline.TimelineGetPresenter.TimelineGet
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

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

@Suppress("EXPERIMENTAL_API_USAGE")
class TimelineGetPresenterTest {

    @get:Rule
    val mockitoInit = MockitoJUnit.rule()!!

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var timelineRepository: TimelineRepository

    @Test
    fun getTimeline_Success() = testCoroutineRule.runBlockingTest {

        given(timelineRepository.get(anyInt())).willReturn(listOf())

        val presenter = TimelineGetPresenter(timelineRepository, 0)

        presenter.get()

        assert(presenter.viewState.value is TimelineGet.Init)

    }

    @Test
    fun addTimeline_Success() = testCoroutineRule.runBlockingTest {

        given(timelineRepository.loadMore(anyInt())).willReturn(listOf())

        val presenter = TimelineGetPresenter(timelineRepository, 0)

        presenter.loadMore()

        assert(presenter.viewState.value is TimelineGet.Add)

    }

    @Test
    fun getTimeline_Fail() = testCoroutineRule.runBlockingTest {

        given(timelineRepository.get(anyInt())).willThrow(NullPointerException())

        val presenter = TimelineGetPresenter(timelineRepository, 0)

        presenter.get()

        assert(presenter.viewState.value is TimelineGet.Error)

    }

}
