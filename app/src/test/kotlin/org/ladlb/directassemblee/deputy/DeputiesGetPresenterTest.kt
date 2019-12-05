package org.ladlb.directassemblee.deputy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.ladlb.directassemblee.PresenterTest
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter.DeputiesGetView
import org.mockito.ArgumentMatchers.anyDouble
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

class DeputiesGetPresenterTest : PresenterTest() {

    private val presenter: DeputiesGetPresenter

    private val view = mock(DeputiesGetView::class.java)

    private val apiRepository = mock(ApiRepository::class.java)

    init {

        presenter = DeputiesGetPresenter(apiRepository, view)
        presenter.context = Dispatchers.Unconfined

    }

    @Test
    fun getDeputies_Success() = runBlocking {

        val result = listOf<Deputy>()

        `when`(apiRepository.getDeputies()).thenReturn(result)

        presenter.getDeputies()

        verify(view, atLeastOnce()).onDeputiesReceived(result)

    }

    @Test
    fun getDeputies_Fail() = runBlocking {

        `when`(apiRepository.getDeputies()).thenThrow(NullPointerException())

        presenter.getDeputies()

        verify(view, atLeastOnce()).onGetDeputiesRequestFailed()

    }

    @Test
    fun getDeputiesCoordinates_Success() = runBlocking {

        val result = listOf<Deputy>()

        `when`(apiRepository.getDeputies(anyDouble(), anyDouble())).thenReturn(result)

        presenter.getDeputies(anyDouble(), anyDouble())

        verify(view, atLeastOnce()).onDeputiesReceived(result)

    }

    @Test
    fun getDeputiesCoordinates_Fail() = runBlocking {

        `when`(apiRepository.getDeputies(anyDouble(), anyDouble())).thenThrow(NullPointerException())

        presenter.getDeputies(anyDouble(), anyDouble())

        verify(view, atLeastOnce()).onGetDeputiesRequestFailed()

    }

}