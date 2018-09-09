package org.ladlb.directassemblee.address

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.view.View
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

internal class SearchAddressItemDecorator(val context: Context) : ItemDecoration() {

    private var paint: Paint

    private val horizontalMargin: Int

    private var separator: Int

    private var verticalMargin: Int

    private var bigVerticalMargin: Int

    private var smallVerticalMargin: Int

    init {
        val resources = context.resources
        separator = resources.getDimensionPixelSize(R.dimen.separator)
        horizontalMargin = resources.getDimensionPixelSize(R.dimen.horizontal_space)
        smallVerticalMargin = resources.getDimensionPixelOffset(R.dimen.small_vertical_space)
        verticalMargin = resources.getDimensionPixelOffset(R.dimen.vertical_space)
        bigVerticalMargin = resources.getDimensionPixelOffset(R.dimen.big_vertical_space)

        paint = Paint()
        paint.strokeWidth = separator.toFloat()
        paint.color = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {

        outRect.left = horizontalMargin
        outRect.right = horizontalMargin

        val itemCount = parent.adapter.itemCount
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

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        val childCount = parent.childCount
        if (childCount > 0) {

            val view = parent.getChildAt(childCount - 1)
            val params = view.layoutParams as RecyclerView.LayoutParams
            val position = params.viewAdapterPosition
            val itemCount = parent.adapter.itemCount

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
