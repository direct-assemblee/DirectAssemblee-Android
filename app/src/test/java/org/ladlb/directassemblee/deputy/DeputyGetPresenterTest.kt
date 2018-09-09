package org.ladlb.directassemblee.deputy

import io.reactivex.Single
import org.junit.Test
import org.ladlb.directassemblee.PresenterTest
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.mockito.ArgumentMatchers.anyInt
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

class DeputyGetPresenterTest : PresenterTest() {

    @Mock
    lateinit var apiRepository: ApiRepository

    @Mock
    lateinit var view: DeputyGetPresenter.GetDeputyView

    @Test
    fun getDeputies_Success() {

        val result = Deputy()

        Mockito.doReturn(Single.just(result)).`when`(apiRepository).getDeputy(anyInt(), anyInt())

        DeputyGetPresenter(view, null).getDeputy(apiRepository, anyInt(), anyInt())
        Mockito.verify(view, Mockito.atLeastOnce()).onDeputyReceived(result)

    }

    @Test
    fun getDeputies_Fail() {

        Mockito.doReturn(Single.error<Deputy>(Throwable())).`when`(apiRepository).getDeputy(anyInt(), anyInt())

        DeputyGetPresenter(view, null).getDeputy(apiRepository, anyInt(), anyInt())
        Mockito.verify(view, Mockito.atLeastOnce()).onGetDeputyRequestFailed()

    }
}