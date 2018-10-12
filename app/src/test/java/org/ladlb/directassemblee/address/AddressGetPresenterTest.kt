package org.ladlb.directassemblee.address

import io.reactivex.Single
import org.junit.Test
import org.ladlb.directassemblee.PresenterTest
import org.ladlb.directassemblee.api.dataGouv.AddressRepository
import org.ladlb.directassemblee.deputy.Deputy
import org.mockito.ArgumentMatchers.anyString
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

class AddressGetPresenterTest : PresenterTest() {

    @Mock
    lateinit var addressRepository: AddressRepository

    @Mock
    lateinit var view: AddressGetPresenter.AddressGetView

    @Test
    fun getDeputies_Success() {

        val result = AddressEnvelope()

        Mockito.doReturn(Single.just(result)).`when`(addressRepository).getAddress(anyString())

        AddressGetPresenter(view, null).get(addressRepository, anyString())
        Mockito.verify(view, Mockito.atLeastOnce()).onAddressesReceived(result.query, result.features)

    }

    @Test
    fun getDeputies_Fail() {

        Mockito.doReturn(Single.error<Array<Deputy>>(Throwable())).`when`(addressRepository).getAddress(anyString())

        AddressGetPresenter(view, null).get(addressRepository, anyString())
        Mockito.verify(view, Mockito.atLeastOnce()).onGetAddressRequestFailed()

    }

}