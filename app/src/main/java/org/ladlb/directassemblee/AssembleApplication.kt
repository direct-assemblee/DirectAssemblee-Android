package org.ladlb.directassemblee

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.google.android.gms.security.ProviderInstaller
import io.fabric.sdk.android.Fabric
import org.ladlb.directassemblee.api.dataGouv.RetrofitAddressRepository
import org.ladlb.directassemblee.api.ladlb.RetrofitApiRepository
import org.ladlb.directassemblee.data.CacheManager
import org.ladlb.directassemblee.firebase.FireBaseAnalyticsManager
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

open class AssembleApplication : Application() {

    private lateinit var apiServices: RetrofitApiRepository

    private lateinit var addressServices: RetrofitAddressRepository

    private lateinit var preferences: PreferencesStorage

    private lateinit var firebaseAnalytics: FireBaseAnalyticsManager

    override fun onCreate() {
        super.onCreate()

        installProviderIfNeeded()

        initFabric()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        apiServices = RetrofitApiRepository(BuildConfig.BASE_URL, cacheDir)
        addressServices = RetrofitAddressRepository(getString(R.string.url_adresse_data_gouv), cacheDir)

        preferences = PreferencesStorageImpl(
                getSharedPreferences(
                        "directAssembee",
                        Context.MODE_PRIVATE
                )
        )

        firebaseAnalytics = FireBaseAnalyticsManager()

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

    fun getAddressServices(): RetrofitAddressRepository = addressServices

    fun getPreferences(): PreferencesStorage = preferences

    fun getFireBaseAnalytics(): FireBaseAnalyticsManager = firebaseAnalytics

    fun getCacheManager(): CacheManager = CacheManager.getInstance()

}
