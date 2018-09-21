package org.ladlb.directassemblee.role

import android.content.res.Resources
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.view.View
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.widget.PlaceholderAdapter

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

class RoleDecorator(resources: Resources) : ItemDecoration() {

    private var smallVerticalMargin = resources.getDimensionPixelOffset(R.dimen.small_vertical_space)

    private var verticalMargin = resources.getDimensionPixelOffset(R.dimen.vertical_space)

    private var horizontalMargin = resources.getDimensionPixelOffset(R.dimen.horizontal_space)

    private var largeHorizontalMargin = resources.getDimensionPixelOffset(R.dimen.large_horizontal_space)

    private var hugeHorizontalMargin = resources.getDimensionPixelOffset(R.dimen.huge_horizontal_space)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {

        val position = parent.getChildAdapterPosition(view)
        val adapter = parent.adapter
        val itemViewType = adapter.getItemViewType(position)

        when (itemViewType) {
            RoleAdapter.typeRole -> {
                outRect.left = horizontalMargin
                outRect.top = verticalMargin
                outRect.right = horizontalMargin
            }
            RoleAdapter.typeRolePosition -> {
                outRect.left = largeHorizontalMargin
                outRect.top = smallVerticalMargin
                outRect.right = horizontalMargin
            }
            RoleAdapter.typeRoleInstance -> {
                outRect.left = hugeHorizontalMargin
                outRect.top = smallVerticalMargin
                outRect.right = horizontalMargin
            }
            PlaceholderAdapter.typePlaceHolder -> {
                outRect.left = horizontalMargin
                outRect.top = verticalMargin
                outRect.right = horizontalMargin
            }
        }

        if (position == adapter.itemCount - 1) {
            outRect.bottom = verticalMargin
        }

    }

}