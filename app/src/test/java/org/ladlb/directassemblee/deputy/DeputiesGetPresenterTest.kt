package org.ladlb.directassemblee.deputy

import io.reactivex.Single
import org.junit.Test
import org.ladlb.directassemblee.PresenterTest
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.mockito.ArgumentMatchers.anyDouble
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

class DeputiesGetPresenterTest : PresenterTest() {

    @Mock
    lateinit var apiRepository: ApiRepository

    @Mock
    lateinit var view: DeputiesGetPresenter.DeputiesGetView

    @Test
    fun getDeputies_Success() {

        val result = arrayOf<Deputy>()

        Mockito.doReturn(Single.just(result)).`when`(apiRepository).getDeputies()

        DeputiesGetPresenter(view, null).getDeputies(apiRepository)
        Mockito.verify(view, Mockito.atLeastOnce()).onDeputiesReceived(result)

    }

    @Test
    fun getDeputies_Fail() {

        Mockito.doReturn(Single.error<Array<Deputy>>(Throwable())).`when`(apiRepository).getDeputies()

        DeputiesGetPresenter(view, null).getDeputies(apiRepository)
        Mockito.verify(view, Mockito.atLeastOnce()).onGetDeputiesRequestFailed()

    }

    @Test
    fun getDeputiesCoordinates_Success() {

        val result = arrayOf<Deputy>()

        Mockito.doReturn(Single.just(result)).`when`(apiRepository).getDeputies(anyDouble(), anyDouble())

        DeputiesGetPresenter(view, null).getDeputies(apiRepository, anyDouble(), anyDouble())
        Mockito.verify(view, Mockito.atLeastOnce()).onDeputiesReceived(result)

    }

    @Test
    fun getDeputiesCoordinates_Fail() {

        Mockito.doReturn(Single.error<Array<Deputy>>(Throwable())).`when`(apiRepository).getDeputies(anyDouble(), anyDouble())

        DeputiesGetPresenter(view, null).getDeputies(apiRepository, anyDouble(), anyDouble())
        Mockito.verify(view, Mockito.atLeastOnce()).onGetDeputiesRequestFailed()

    }

}