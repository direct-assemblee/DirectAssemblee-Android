package org.ladlb.directassemblee.helper

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat

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

class DrawableHelper {

    companion object {

        fun getDrawableTintByColorId(resources: Resources, drawableId: Int, @ColorRes colorId: Int): Drawable {

            return getDrawableTintByColor(
                    resources,
                    drawableId,
                    ResourcesCompat.getColor(
                            resources,
                            colorId,
                            null
                    )
            )

        }

        fun getDrawableTintByColor(resources: Resources, drawableId: Int, @ColorInt color: Int): Drawable {

            val drawable = ResourcesCompat.getDrawable(
                    resources,
                    drawableId,
                    null
            )!!.mutate()
            DrawableCompat.setTint(
                    drawable,
                    color
            )

            return drawable

        }

    }

}