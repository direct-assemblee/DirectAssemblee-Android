package org.ladlb.directassemblee.analytics.dependencies

import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.ladlb.directassemblee.analytics.AnalyticsActivityLifeCycleCallBacks
import org.ladlb.directassemblee.analytics.AnalyticsFragmentLifecycleCallbacks
import org.ladlb.directassemblee.analytics.AnalyticsHelper
import org.ladlb.directassemblee.analytics.BuildConfig
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsManager

object Dependencies {

    val analyticsModule = module {

        single {
            FirebaseAnalyticsManager(
                    if (BuildConfig.TRACKING_ENABLED) FirebaseAnalytics.getInstance(androidContext()) else null
            )
        }

        single {
            AnalyticsHelper()
        }

        single {
            AnalyticsFragmentLifecycleCallbacks(get())
        }

        single {
            AnalyticsActivityLifeCycleCallBacks(get())
        }

    }

}
