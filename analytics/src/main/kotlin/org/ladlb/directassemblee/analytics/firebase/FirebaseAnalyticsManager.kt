package org.ladlb.directassemblee.analytics.firebase

import android.os.Bundle
import androidx.annotation.NonNull
import com.google.firebase.analytics.FirebaseAnalytics

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

class FirebaseAnalyticsManager(private val firebaseAnalytics: FirebaseAnalytics? = null) {

    fun setCurrentScreen(tagName: String, className: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, tagName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, className)
        logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun logEvent(event: String, bundle: Bundle = Bundle()) {
        firebaseAnalytics?.logEvent(event, bundle)
    }

    fun setUserProperty(@NonNull key: String, @NonNull value: String?) {
        firebaseAnalytics?.setUserProperty(key, value)
    }

    fun clearUserProperty(@NonNull vararg key: String) {
        firebaseAnalytics?.let {
            key.map {
                firebaseAnalytics.setUserProperty(it, null)
            }
        }
    }

}