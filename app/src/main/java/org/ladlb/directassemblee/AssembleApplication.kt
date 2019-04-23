package org.ladlb.directassemblee

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.google.android.gms.security.ProviderInstaller
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.fabric.sdk.android.Fabric
import org.ladlb.directassemblee.api.ladlb.RetrofitApiRepository
import org.ladlb.directassemblee.data.CacheManager
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.helper.MetricHelper
import org.ladlb.directassemblee.preferences.PreferencesStorage
import org.ladlb.directassemblee.preferences.PreferencesStorageImpl

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

open class AssembleApplication : DaggerApplication() {

    private lateinit var apiServices: RetrofitApiRepository

    private lateinit var preferences: PreferencesStorage

    private lateinit var firebaseAnalytics: FirebaseAnalyticsManager

    override fun onCreate() {
        super.onCreate()

        installProviderIfNeeded()

        initFabric()

        initPicasso()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        preferences = PreferencesStorageImpl(
                getSharedPreferences(
                        "directAssembee",
                        Context.MODE_PRIVATE
                )
        )

        firebaseAnalytics = FirebaseAnalyticsManager()

    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }

    private fun initPicasso() {

        val picassoBuilder = Picasso.Builder(this)
        if (BuildConfig.DEBUG) {
            picassoBuilder.loggingEnabled(true)
            picassoBuilder.listener { _, uri, exception ->
                Log.e("Picasso", "onImageFailed : uri : " + uri.toString() + ", exception : " + exception.message)
            }
        }
        Picasso.setSingletonInstance(picassoBuilder.build())

    }

    open fun initFabric() {
        if (BuildConfig.CRASH_TRACKER_ENABLED) {
            val fabric = Fabric.Builder(this)
                    .kits(Crashlytics())
                    .debuggable(true)
                    .build()
            Fabric.with(fabric)
        }
    }

    private fun installProviderIfNeeded() {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: Exception) {
            MetricHelper.track(e)
        }
    }

    fun getApiServices(): RetrofitApiRepository = apiServices

    fun getPreferences(): PreferencesStorage = preferences

    fun getFireBaseAnalytics(): FirebaseAnalyticsManager = firebaseAnalytics

    fun getCacheManager(): CacheManager = CacheManager.getInstance()

}
