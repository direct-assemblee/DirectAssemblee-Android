package org.ladlb.directassemblee.synthesis

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.ladlb.directassemblee.synthesis.rate.RateGetPresenter
import org.ladlb.directassemblee.synthesis.rate.RateGetPresenter.RateGet
import org.ladlb.directassemblee.synthesis.rate.RateRepository
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
class RateGetPresenterTest {

    @get:Rule
    val mockitoInit = MockitoJUnit.rule()!!

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var rateRepository: RateRepository

    @Test
    fun getActivityRates_Success() = testCoroutineRule.runBlockingTest {

        given(rateRepository.getActivityRates()).willReturn(listOf())

        val presenter = RateGetPresenter(rateRepository)

        assert(presenter.viewState.value is RateGet.Result)

    }

    @Test
    fun getActivityRates_Fail() = runBlocking {

        given(rateRepository.getActivityRates()).willThrow(NullPointerException())

        val presenter = RateGetPresenter(rateRepository)

        assert(presenter.viewState.value is RateGet.Error)

    }

}