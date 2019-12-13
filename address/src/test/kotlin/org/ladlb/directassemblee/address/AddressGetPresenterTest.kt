package org.ladlb.directassemblee.address

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.ladlb.directassemblee.address.AddressGetPresenter.AddressGet
import org.ladlb.directassemblee.model.AddressEnvelope
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.mock
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
class AddressGetPresenterTest {

    @get:Rule
    val mockitoInit = MockitoJUnit.rule()!!

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var presenter: AddressGetPresenter

    @Mock
    private lateinit var addressRepository: AddressRepository

    @Before
    fun setup() {
        presenter = AddressGetPresenter(addressRepository)
    }

    @Test
    fun getAddress_Success() = testCoroutineRule.runBlockingTest {

        val enveloppe = mock(AddressEnvelope::class.java)
        given(enveloppe.query).willReturn("test")
        given(enveloppe.features).willReturn(listOf())
        given(addressRepository.getAddress(anyString())).willReturn(enveloppe)

        presenter.get(anyString())

        assert(presenter.viewState.value is AddressGet.Result)

    }

    @Test
    fun getAddress_Fail() = testCoroutineRule.runBlockingTest {

        given(addressRepository.getAddress(anyString())).willThrow(NullPointerException())

        presenter.get(anyString())

        assert(presenter.viewState.value is AddressGet.Error)

    }

}