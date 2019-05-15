package org.ladlb.directassemblee.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

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

class ViewHelper {

    companion object {

        fun hideKeyboard(activity: Activity) {
            val view = activity.currentFocus
            if (view != null) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun hideView(view: View?, duration: Long) {
            view?.animate()?.scaleY(0f)?.scaleX(0f)?.setDuration(duration)?.start()
        }

        fun showView(view: View?, duration: Long) {
            view?.animate()?.scaleY(1f)?.scaleX(1f)?.setDuration(duration)?.start()
        }

        fun changeTextAlpha(view: TextView?, alpha: Float, duration: Long) {
            view?.animate()?.alpha(alpha)?.setDuration(duration)?.start()
        }

    }

}
