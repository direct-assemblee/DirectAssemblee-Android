package org.ladlb.directassemblee

import android.app.Application
import android.content.Context
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
import org.ladlb.directassemblee.vote.Vote
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

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

        @Singleton
        @JvmStatic
        @Provides
        fun provideCacheDir(context: Context): File {
            return context.cacheDir
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
