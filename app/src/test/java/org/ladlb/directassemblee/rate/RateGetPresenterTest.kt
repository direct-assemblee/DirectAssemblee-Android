package org.ladlb.directassemblee.rate

import io.reactivex.Single
import org.junit.Test
import org.ladlb.directassemblee.PresenterTest
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.deputy.Deputy
import org.mockito.Mock
import org.mockito.Mockito

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

class RateGetPresenterTest : PresenterTest() {

    @Mock
    lateinit var apiRepository: ApiRepository

    @Mock
    lateinit var view: RateGetPresenter.RateGetView

    @Test
    fun getActivityRates_Success() {

        val result = arrayOf<Rate>()

        Mockito.doReturn(Single.just(result)).`when`(apiRepository).getActivityRates()

        RateGetPresenter(view, null).getActivityRates(apiRepository)
        Mockito.verify(view, Mockito.atLeastOnce()).onActivityRatesReceived(result)

    }

    @Test
    fun getActivityRates_Fail() {

        Mockito.doReturn(Single.error<Array<Deputy>>(Throwable())).`when`(apiRepository).getActivityRates()

        RateGetPresenter(view, null).getActivityRates(apiRepository)
        Mockito.verify(view, Mockito.atLeastOnce()).onGetActivityRatesRequestFailed()

    }

}