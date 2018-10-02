package org.ladlb.directassemblee.api.dataGouv

import androidx.annotation.WorkerThread
import com.google.gson.GsonBuilder
import io.reactivex.Single
import org.ladlb.directassemblee.address.AddressEnvelope
import org.ladlb.directassemblee.api.RetrofitBaseRepository
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

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

class RetrofitAddressRepository(baseUrl: String, cacheDir: File) : RetrofitBaseRepository(cacheDir), AddressRepository {

    private val services: AddressServices

    @WorkerThread
    override fun getAddress(query: String): Single<AddressEnvelope> = services.getAddress(query)

    init {

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setPrettyPrinting()

        val gson = gsonBuilder.create()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        services = retrofit.create(AddressServices::class.java)

    }

}
