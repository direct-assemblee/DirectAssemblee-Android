package org.ladlb.directassemblee.address

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.ladlb.directassemblee.PresenterTest
import org.ladlb.directassemblee.address.AddressGetPresenter.AddressGetView
import org.ladlb.directassemblee.api.dataGouv.AddressRepository
import org.mockito.ArgumentMatchers.anyString
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

class AddressGetPresenterTest : PresenterTest() {

    private val presenter: AddressGetPresenter

    private val view = mock(AddressGetView::class.java)

    private val addressRepository = mock(AddressRepository::class.java)

    init {

        presenter = AddressGetPresenter(view)
        presenter.context = Dispatchers.Unconfined

    }

    @Test
    fun getAddress_Success() = runBlocking {

        val result = AddressEnvelope()

        `when`(addressRepository.getAddress(anyString())).thenReturn(result)

        presenter.get(addressRepository, anyString())

        verify(view, atLeastOnce()).onAddressesReceived(result.query, result.features)

    }

    @Test
    fun getAddress_Fail() = runBlocking {

        `when`(addressRepository.getAddress(anyString())).thenThrow(NullPointerException())

        presenter.get(addressRepository, anyString())

        verify(view, atLeastOnce()).onGetAddressRequestFailed()

    }

}