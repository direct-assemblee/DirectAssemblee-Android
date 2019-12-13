package org.ladlb.directassemblee.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import org.ladlb.directassemblee.address.AddressGetPresenter.AddressGet.Error
import org.ladlb.directassemblee.address.AddressGetPresenter.AddressGet.Result
import org.ladlb.directassemblee.core.AbstractViewModel
import org.ladlb.directassemblee.model.Address

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

class AddressGetPresenter(private val addressRepository: AddressRepository) : AbstractViewModel() {

    sealed class AddressGet {
        class Result(val query: String, val addresses: List<Address>) : AddressGet()
        object Error : AddressGet()
    }

    private val _viewState = MutableLiveData<AddressGet>()
    val viewState: LiveData<AddressGet> = _viewState

    fun get(query: String) {

        launch {
            try {
                addressRepository.getAddress(query).let {
                    _viewState.postValue(Result(query = it.query, addresses = it.features))
                }
            } catch (e: Exception) {
                _viewState.postValue(Error)
            }
        }

    }

}
