package org.ladlb.directassemblee.network.address.dependencies

import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.ladlb.directassemblee.network.address.AddressServices
import org.ladlb.directassemblee.network.address.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Dependencies {

    val networkApiAdressModule = module {

        single {

            val gsonBuilder = GsonBuilder()
            gsonBuilder.setPrettyPrinting()

            val gson = gsonBuilder.create()

            val retrofit = Retrofit.Builder()
                    .baseUrl(androidContext().getString(R.string.url_adresse_data_gouv))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(get())
                    .build()

            retrofit.create(AddressServices::class.java)

        }

    }

}
