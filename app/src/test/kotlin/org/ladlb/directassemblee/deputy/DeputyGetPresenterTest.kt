package org.ladlb.directassemblee.deputy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.ladlb.directassemblee.PresenterTest
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.deputy.DeputyGetPresenter.DeputyGetView
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
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
class DeputyGetPresenterTest : PresenterTest() {

    private val presenter: DeputyGetPresenter

    private val view = mock(DeputyGetView::class.java)

    private val apiRepository = mock(ApiRepository::class.java)

    init {

        presenter = DeputyGetPresenter(view)
        presenter.context = Dispatchers.Unconfined

    }

    @Test
    fun getDeputies_Success() = runBlocking {

        val result = Deputy()

        `when`(apiRepository.getDeputy(anyInt(), anyInt())).thenReturn(result)

        presenter.getDeputy(apiRepository, anyInt(), anyInt())

        verify(view, Mockito.atLeastOnce()).onDeputyReceived(result)

    }

    @Test
    fun getDeputies_Fail() = runBlocking {

        `when`(apiRepository.getDeputy(anyInt(), anyInt())).thenThrow(NullPointerException())

        presenter.getDeputy(apiRepository, anyInt(), anyInt())

        Mockito.verify(view, Mockito.atLeastOnce()).onGetDeputyRequestFailed()

    }
}