package org.ladlb.directassemblee.helper

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.NonNull
import android.support.annotation.StyleRes
import org.ladlb.directassemblee.R

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

class ColorHelper {

    companion object {

        /**
         * Returns the color for the given theme.
         *
         * @param context the context.
         * @param attr the attribute to resolve.
         * @param resId the theme resource id.
         * @return the color for the given theme.
         */
        @ColorInt
        fun getColor(@NonNull context: Context, attr: Int, @StyleRes resId: Int): Int {
            val attrs = intArrayOf(attr)
            val a = context.obtainStyledAttributes(resId, attrs)
            val color = a.getColor(0, 0)
            a.recycle()
            return color
        }

        /**
         * Returns the primary color.
         *
         * @param context the context.
         * @return the primary color.
         */
        @ColorInt
        fun getColorPrimary(@NonNull context: Context): Int = getColor(
                context,
                R.attr.colorPrimary,
                0
        )

        /**
         * Returns the secondary text color.
         *
         * @param context the context.
         * @return the secondary text color.
         */
        @ColorInt
        fun getTextColorSecondary(@NonNull context: Context): Int = getColor(
                context,
                android.R.attr.textColorSecondary,
                0
        )

    }

}