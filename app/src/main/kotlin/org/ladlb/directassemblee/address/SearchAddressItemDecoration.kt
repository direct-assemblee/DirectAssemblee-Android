package org.ladlb.directassemblee.address

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
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

internal class SearchAddressItemDecoration(resources: Resources) : ItemDecoration() {

    private var paint: Paint = Paint()

    private val horizontalMargin: Int = resources.getDimensionPixelSize(R.dimen.horizontal_space)

    private var separator: Int = resources.getDimensionPixelSize(R.dimen.separator)

    private var verticalMargin: Int = resources.getDimensionPixelOffset(R.dimen.vertical_space)

    private var bigVerticalMargin: Int = resources.getDimensionPixelOffset(R.dimen.big_vertical_space)

    private var smallVerticalMargin: Int = resources.getDimensionPixelOffset(R.dimen.small_vertical_space)

    init {
        paint.strokeWidth = separator.toFloat()
        paint.color = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {

        outRect.left = horizontalMargin
        outRect.right = horizontalMargin

        val itemCount = parent.adapter!!.itemCount
        val position = parent.getChildAdapterPosition(view)

        if (position == itemCount - 1) {
            outRect.bottom = verticalMargin
            if (itemCount == 1) {
                outRect.top = smallVerticalMargin
            } else {
                outRect.top = bigVerticalMargin
            }
        } else {
            outRect.top = verticalMargin
        }

    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: State) {

        val childCount = parent.childCount
        if (childCount > 0) {

            val view = parent.getChildAt(childCount - 1)
            val params = view.layoutParams as RecyclerView.LayoutParams
            val position = params.viewAdapterPosition
            val itemCount = parent.adapter!!.itemCount

            if (position == state.itemCount - 1 && itemCount > 1) {
                val y = view.top.toFloat() - smallVerticalMargin - separator / 2
                c.drawLine(
                        view.left.toFloat(),
                        y,
                        view.right.toFloat(),
                        y,
                        paint
                )
            }

        }

    }

}
