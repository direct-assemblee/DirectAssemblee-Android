package org.ladlb.directassemblee.address

import kotlinx.coroutines.launch
import org.ladlb.directassemblee.AbstractPresenter
import org.ladlb.directassemblee.address.AddressGetPresenter.AddressGetView
import org.ladlb.directassemblee.api.dataGouv.AddressRepository

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

class AddressGetPresenter(view: AddressGetView) : AbstractPresenter<AddressGetView>(view) {

    fun get(addressRepository: AddressRepository, query: String) {

        launch {
            try {
                val result = addressRepository.getAddress(query)
                view?.onAddressesReceived(result.query, result.features)
            } catch (e: Throwable) {
                view?.onGetAddressRequestFailed()
            }
        }

    }

    interface AddressGetView : BaseView {

        fun onAddressesReceived(query: String, addresses: Array<Address>)

        fun onGetAddressRequestFailed()

    }

}