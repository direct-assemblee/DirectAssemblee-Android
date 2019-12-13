package org.ladlb.directassemblee.ballot.deputy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.ladlb.directassemblee.deputy.DeputyGetPresenter
import org.ladlb.directassemblee.deputy.DeputyGetPresenter.DeputyGet
import org.ladlb.directassemblee.deputy.DeputyRepository
import org.ladlb.directassemblee.model.Deputy
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
class DeputyGetPresenterTest {

    @get:Rule
    val mockitoInit = MockitoJUnit.rule()!!

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var presenter: DeputyGetPresenter

    @Mock
    private lateinit var deputyRepository: DeputyRepository

    @Before
    fun setup() {
        presenter = DeputyGetPresenter(deputyRepository)
    }

    @Test
    fun getDeputies_Success() = testCoroutineRule.runBlockingTest {

        given(deputyRepository.getDeputy(anyInt(), anyInt())).willReturn(Deputy())

        presenter.getDeputy(anyInt(), anyInt())

        assert(presenter.viewState.value is DeputyGet.Result)

    }

    @Test
    fun getDeputies_Fail() = testCoroutineRule.runBlockingTest {

        given(deputyRepository.getDeputy(anyInt(), anyInt())).willThrow(NullPointerException())

        presenter.getDeputy(anyInt(), anyInt())

        assert(presenter.viewState.value is DeputyGet.Error)

    }
}