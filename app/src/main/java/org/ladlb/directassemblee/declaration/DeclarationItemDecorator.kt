package org.ladlb.directassemblee.declaration

import android.content.res.Resources
import android.graphics.Rect
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

internal class DeclarationItemDecorator(resources: Resources) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    private val verticalMargin = resources.getDimensionPixelOffset(R.dimen.small_vertical_space)

    private val horizontalMargin = resources.getDimensionPixelOffset(R.dimen.horizontal_space)

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {

        val childAdapterPosition = parent.getChildAdapterPosition(view)

        outRect.top = if (childAdapterPosition == 0) verticalMargin else 0
        outRect.left = horizontalMargin
        outRect.right = horizontalMargin
        outRect.bottom = if (childAdapterPosition == parent.adapter!!.itemCount - 1) verticalMargin else 0

    }

}
