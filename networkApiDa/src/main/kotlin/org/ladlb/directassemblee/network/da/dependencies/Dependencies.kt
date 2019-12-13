package org.ladlb.directassemblee.network.da.dependencies

import com.google.gson.GsonBuilder
import org.koin.dsl.module
import org.ladlb.directassemblee.model.Vote
import org.ladlb.directassemblee.network.da.ApiServices
import org.ladlb.directassemblee.network.da.BuildConfig
import org.ladlb.directassemblee.network.da.VoteDeserializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Dependencies {

    val networdApiDaModule = module {

        single {

            val gsonBuilder = GsonBuilder()
            gsonBuilder.setPrettyPrinting()
            gsonBuilder.setDateFormat("dd/MM/yy")
            gsonBuilder.registerTypeAdapter(Vote::class.java, VoteDeserializer())

            val gson = gsonBuilder.create()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(get())
                    .build()
            retrofit.create(ApiServices::class.java)

        }

    }

}
