package org.ladlb.directassemblee.network.core.dependencies

import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.ladlb.directassemblee.network.core.BuildConfig
import org.ladlb.directassemblee.network.core.NetworkCacheInterceptor
import java.util.concurrent.TimeUnit

object Dependencies {

    private const val TIMEOUT: Long = 15

    val networkCoreModule = module {

        single {
            androidContext().cacheDir
        }

        single {

            val cacheSize = 10 * 1024 * 1024 // 10 MB
            val cache = Cache(get(), cacheSize.toLong())

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            @Suppress("ConstantConditionIf")
            OkHttpClient.Builder()
                    .cache(cache)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(httpLoggingInterceptor.apply { httpLoggingInterceptor.level = if (BuildConfig.LOG_ENABLED) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }
                    )
                    .addNetworkInterceptor(NetworkCacheInterceptor())
                    .build()

        }

    }

}
