package org.ladlb.directassemblee.analytics

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class AnalyticsActivityLifeCycleCallBacks(private val analyticsFragmentLifecycleCallbacks: AnalyticsFragmentLifecycleCallbacks) : ActivityLifecycleCallbacks {

    private var currentActivity: Activity? = null

    override fun onActivityPaused(activity: Activity) {
        if (activity == currentActivity) {
            currentActivity = activity
        }
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityStopped(activity: Activity) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(
                    analyticsFragmentLifecycleCallbacks
            )
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                    analyticsFragmentLifecycleCallbacks,
                    true
            )
        }
    }

    override fun onActivityResumed(activity: Activity) {

    }

}
