package org.ladlb.directassemblee.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.IntDef
import androidx.fragment.app.DialogFragment

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

abstract class AbstractDialog : DialogFragment() {

    private var mAbstractActivity: AbstractActivity? = null

    private var mTarget: Any? = null

    private var mRequestCode: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mAbstractActivity = activity as AbstractActivity
    }

    override fun onDetach() {
        super.onDetach()
        mAbstractActivity = null
    }

    fun setTarget(target: Any, requestCode: Int): AbstractDialog {
        mTarget = target
        mRequestCode = requestCode
        return this
    }

    protected fun sendResult(@ResultCode result: Int, data: Intent) {

        if (mTarget is AbstractFragment) {
            (mTarget as AbstractFragment).onDialogFragmentResult(
                    mRequestCode,
                    result,
                    data
            )
        }

        if (mTarget is AbstractActivity) {
            (mTarget as AbstractActivity).onDialogFragmentResult(
                    mRequestCode,
                    result,
                    data
            )
        }

        dialog?.cancel()

    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(Activity.RESULT_OK, Activity.RESULT_CANCELED)
    annotation class ResultCode

}
