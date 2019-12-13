package org.ladlb.directassemblee.analytics

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics

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

class AnalyticsHelper {

    private val TAG = AnalyticsHelper::class.java.name

    fun track(throwable: Throwable) {
        throwable.message?.let {
            track(it)
        }
    }

    fun track(event: String) {

        if (BuildConfig.LOG_ENABLED) {
            Log.d(TAG, event)
        }

        if (BuildConfig.CRASH_TRACKER_ENABLED) {
            FirebaseCrashlytics.getInstance().log(
                    event
            )
        }

    }

}