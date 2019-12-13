package org.ladlb.directassemblee.core.helper

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import org.ladlb.directassemblee.core.R

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

/**
 * Returns the color for the given theme.
 *
 * @param attr the attribute to resolve.
 * @param resId the theme resource id.
 * @return the color for the given theme.
 */
@ColorInt
fun Context.getColor(attr: Int, @StyleRes resId: Int): Int {
    val attrs = intArrayOf(attr)
    val a = obtainStyledAttributes(resId, attrs)
    val color = a.getColor(0, 0)
    a.recycle()
    return color
}

/**
 * Returns the primary color.
 *
 * @return the primary color.
 */
@ColorInt
fun Context.getColorPrimary(): Int = getColor(R.attr.colorPrimary, 0)

/**
 * Returns the secondary text color.
 *
 * @return the secondary text color.
 */
@ColorInt
fun Context.getTextColorSecondary(): Int = getColor(android.R.attr.textColorSecondary, 0)
