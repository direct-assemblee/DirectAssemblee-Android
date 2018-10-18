package org.ladlb.directassemblee.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_more.view.*
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.helper.ColorHelper
import org.ladlb.directassemblee.helper.DrawableHelper

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

class MoreView : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {

        inflate(
                context,
                R.layout.view_more,
                this
        )

        val color = ColorHelper.getColor(
                context,
                R.attr.colorAccent,
                0
        )

        textViewMore.setTextColor(color)
        textViewMore.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                DrawableHelper.getDrawableTintByColor(
                        resources,
                        R.drawable.ic_chevron_right_24dp,
                        color
                ),
                null
        )


    }

}