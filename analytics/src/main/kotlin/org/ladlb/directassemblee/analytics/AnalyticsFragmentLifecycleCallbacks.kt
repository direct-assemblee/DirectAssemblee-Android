package org.ladlb.directassemblee.analytics

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.core.AbstractFragment

class AnalyticsFragmentLifecycleCallbacks(private val firebaseAnalyticsManager: FirebaseAnalyticsManager) : FragmentLifecycleCallbacks() {

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
        if (f is AbstractFragment) {
            firebaseAnalyticsManager.setCurrentScreen(
                    f.getTagName(),
                    f.getClassName()
            )
        }
    }

}
