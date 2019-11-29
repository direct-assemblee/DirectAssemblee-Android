package org.ladlb.directassemblee

import androidx.appcompat.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.google.android.gms.security.ProviderInstaller
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.fabric.sdk.android.Fabric
import org.ladlb.directassemblee.helper.MetricHelper

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

    override fun onCreate() {
        super.onCreate()

        installProviderIfNeeded()

        initFabric()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
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

}
