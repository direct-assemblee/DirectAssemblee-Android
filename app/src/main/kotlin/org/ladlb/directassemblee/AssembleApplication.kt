package org.ladlb.directassemblee

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.security.ProviderInstaller
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.android.inject
import org.ladlb.directassemblee.analytics.AnalyticsHelper

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

    private val analyticsHelper: AnalyticsHelper by inject()

    override fun onCreate() {
        super.onCreate()

        installProviderIfNeeded()

        initCrashlytics()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

    }

    open fun initCrashlytics() {
        if (!BuildConfig.CRASH_TRACKER_ENABLED) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        }
    }

    private fun installProviderIfNeeded() {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: Exception) {
            analyticsHelper.track(e)
        }
    }

}
