package org.ladlb.directassemblee.rate

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.ladlb.directassemblee.PresenterTest
import org.ladlb.directassemblee.api.ladlb.ApiRepository
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

@ExperimentalCoroutinesApi
class RateGetPresenterTest : PresenterTest() {

    private val presenter: RateGetPresenter

    private val view = mock(RateGetPresenter.RateGetView::class.java)

    private val apiRepository = mock(ApiRepository::class.java)

    init {

        presenter = RateGetPresenter(view, null)
        presenter.context = Dispatchers.Unconfined

    }

    @Test
    fun getActivityRates_Success() = runBlocking {

        val result = arrayOf<Rate>()

        `when`(apiRepository.getActivityRates()).thenReturn(result)

        presenter.getActivityRates(apiRepository)

        verify(view, atLeastOnce()).onActivityRatesReceived(result)

    }

    @Test
    fun getActivityRates_Fail() = runBlocking {

        `when`(apiRepository.getActivityRates()).thenThrow(NullPointerException())

        presenter.getActivityRates(apiRepository)

        verify(view, atLeastOnce()).onGetActivityRatesRequestFailed()

    }

}