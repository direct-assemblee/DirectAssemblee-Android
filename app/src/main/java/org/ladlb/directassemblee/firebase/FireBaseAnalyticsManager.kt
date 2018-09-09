package org.ladlb.directassemblee.firebase

import android.app.Activity
import android.os.Bundle
import android.support.annotation.NonNull
import com.google.firebase.analytics.FirebaseAnalytics
import org.ladlb.directassemblee.AbstractBottomSheetDialogFragment
import org.ladlb.directassemblee.AbstractFragment
import org.ladlb.directassemblee.AbstractPreferenceFragment
import org.ladlb.directassemblee.BuildConfig
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.UserProperty

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

class FireBaseAnalyticsManager {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun initInstance(activity: Activity) {
        if (BuildConfig.TRACKING_ENABLED) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(activity)

        }
    }

    fun setCurrentScreen(activity: Activity, fragment: AbstractFragment) {
        if (BuildConfig.TRACKING_ENABLED) {
            firebaseAnalytics.setCurrentScreen(
                    activity,
                    fragment.getTagName(),
                    fragment.getClassName()
            )
        }
    }

    fun setCurrentScreen(activity: Activity, fragment: AbstractPreferenceFragment) {
        if (BuildConfig.TRACKING_ENABLED) {
            firebaseAnalytics.setCurrentScreen(
                    activity,
                    fragment.getTagName(),
                    fragment.getClassName()
            )
        }
    }

    fun setCurrentScreen(activity: Activity, fragment: AbstractBottomSheetDialogFragment) {
        if (BuildConfig.TRACKING_ENABLED) {
            firebaseAnalytics.setCurrentScreen(
                    activity,
                    fragment.getTagName(),
                    fragment.getClassName()
            )
        }
    }

    fun logEvent(event: String) {
        if (BuildConfig.TRACKING_ENABLED) {
            firebaseAnalytics.logEvent(event, Bundle())
        }
    }

    fun logEvent(event: String, bundle: Bundle) {
        if (BuildConfig.TRACKING_ENABLED) {
            firebaseAnalytics.logEvent(event, bundle)
        }
    }

    fun setUserDeputyProperties(@NonNull deputy: Deputy) {
        if (BuildConfig.TRACKING_ENABLED) {
            firebaseAnalytics.setUserProperty(UserProperty.PARLIAMENT_GROUP, deputy.parliamentGroup)
            firebaseAnalytics.setUserProperty(UserProperty.DISTRICT, deputy.getCompleteLocality())
        }
    }

    fun clearUserDeputyProperties() {
        if (BuildConfig.TRACKING_ENABLED) {
            firebaseAnalytics.setUserProperty(UserProperty.PARLIAMENT_GROUP, null)
            firebaseAnalytics.setUserProperty(UserProperty.DISTRICT, null)
        }
    }

}