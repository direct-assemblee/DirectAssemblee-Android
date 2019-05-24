package org.ladlb.directassemblee

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsManager
import javax.inject.Inject

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

abstract class AbstractBottomSheetDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var firebaseAnalyticsManager: FirebaseAnalyticsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidSupportInjection.inject(this)
    }

    override fun onResume() {
        super.onResume()
        updateCurrentScreen()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        updateCurrentScreen()
    }

    private fun updateCurrentScreen() {
        if (userVisibleHint && activity is AbstractActivity) {
            firebaseAnalyticsManager.setCurrentScreen((activity as AbstractActivity), this)
        }
    }

    open fun getTagName(): String = getClassName()

    abstract fun getClassName(): String

}
