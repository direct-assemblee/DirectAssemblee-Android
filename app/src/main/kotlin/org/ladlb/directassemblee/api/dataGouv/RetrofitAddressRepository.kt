package org.ladlb.directassemblee.api.dataGouv

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ladlb.directassemblee.address.AddressEnvelope
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

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

@Singleton
class RetrofitAddressRepository @Inject constructor(private val addressServices: AddressServices, var context: CoroutineContext = Dispatchers.IO) : AddressRepository {

    override suspend fun getAddress(query: String): AddressEnvelope = withContext(context) {
        addressServices.getAddressAsync(query).await()
    }

}
