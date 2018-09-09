package org.ladlb.directassemblee.api

import android.text.TextUtils
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.ladlb.directassemblee.BuildConfig
import org.ladlb.directassemblee.api.ladlb.ApiException
import java.io.File
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

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

abstract class RetrofitBaseRepository(cacheDir: File) {

    private val timeout: Long = 15

    val okHttpClient: OkHttpClient

    companion object {

        const val CACHE_CONTROL = "Cache-Control"

    }

    init {

        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(cacheDir, cacheSize.toLong())

        @Suppress("ConstantConditionIf")
        okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(
                        if (BuildConfig.LOG_ENABLED) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
                )
                .addNetworkInterceptor(NetworkInterceptor())
                .addNetworkInterceptor(NetworkCacheInterceptor())
                .build()

    }

    class NetworkInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {

            val request = chain.request()
            val response = chain.proceed(request)

            if (response.isSuccessful || response.code() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                return response
            } else {
                throw ApiException(response)
            }
        }

    }

    class NetworkCacheInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {

            val request = chain.request()
            val response = chain.proceed(request)
            val cacheHeader = request.header(CACHE_CONTROL)

            return if (!response.isSuccessful || TextUtils.isEmpty(cacheHeader)) {
                response
            } else {
                response.newBuilder()
                        .header(CACHE_CONTROL, cacheHeader!!)
                        .build()
            }

        }

    }

}