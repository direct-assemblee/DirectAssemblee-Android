package org.ladlb.directassemblee.api

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response

class NetworkCacheInterceptor : Interceptor {

    companion object {
        private const val CACHE_CONTROL = "Cache-Control"
    }

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