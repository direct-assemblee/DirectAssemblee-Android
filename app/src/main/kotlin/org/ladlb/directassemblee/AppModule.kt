package org.ladlb.directassemblee

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.ladlb.directassemblee.api.NetworkCacheInterceptor
import org.ladlb.directassemblee.api.dataGouv.AddressServices
import org.ladlb.directassemblee.api.ladlb.ApiServices
import org.ladlb.directassemblee.api.ladlb.VoteDeserializer
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.vote.Vote
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
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

@Module
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideContext(application: Application): Context

    @Module
    companion object {

        private const val TIMEOUT: Long = 15

        @Provides
        @JvmStatic
        @Singleton
        fun provideCoroutineContext(): CoroutineContext = Dispatchers.IO

        @Provides
        @JvmStatic
        @Singleton
        fun provideFirebaseAnalyticsManager(): FirebaseAnalyticsManager = FirebaseAnalyticsManager()

        @Singleton
        @JvmStatic
        @Provides
        fun provideCacheDir(context: Context): File {
            return context.cacheDir
        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                    "directAssembee",
                    Context.MODE_PRIVATE
            )
        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideOkHttp(cacheDir: File): OkHttpClient {

            val cacheSize = 10 * 1024 * 1024 // 10 MB
            val cache = Cache(cacheDir, cacheSize.toLong())

            @Suppress("ConstantConditionIf")
            return OkHttpClient.Builder()
                    .cache(cache)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().setLevel(
                            if (BuildConfig.LOG_ENABLED) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
                    )
                    .addNetworkInterceptor(NetworkCacheInterceptor())
                    .build()

        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideAddressServices(context: Context, okHttpClient: OkHttpClient): AddressServices {

            val gsonBuilder = GsonBuilder()
            gsonBuilder.setPrettyPrinting()

            val gson = gsonBuilder.create()

            val retrofit = Retrofit.Builder()
                    .baseUrl(context.getString(R.string.url_adresse_data_gouv))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(okHttpClient)
                    .build()
            return retrofit.create(AddressServices::class.java)

        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideApiServices(okHttpClient: OkHttpClient): ApiServices {

            val gsonBuilder = GsonBuilder()
            gsonBuilder.setPrettyPrinting()
            gsonBuilder.setDateFormat("dd/MM/yy")
            gsonBuilder.registerTypeAdapter(Vote::class.java, VoteDeserializer())

            val gson = gsonBuilder.create()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
            return retrofit.create(ApiServices::class.java)

        }

    }

}
