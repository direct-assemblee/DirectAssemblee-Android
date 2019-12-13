package org.ladlb.directassemblee.mandate

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import org.ladlb.directassemblee.core.widget.PlaceholderAdapter

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

class MandateItemDecoration(resources: Resources) : ItemDecoration() {

    private var verticalMargin = resources.getDimensionPixelOffset(R.dimen.vertical_space)

    private var horizontalMargin = resources.getDimensionPixelOffset(R.dimen.horizontal_space)

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {

        val position = parent.getChildAdapterPosition(view)
        val adapter = parent.adapter

        when (adapter!!.getItemViewType(position)) {
            MandateAdapter.typeItem -> {
                outRect.left = horizontalMargin
                outRect.top = verticalMargin
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