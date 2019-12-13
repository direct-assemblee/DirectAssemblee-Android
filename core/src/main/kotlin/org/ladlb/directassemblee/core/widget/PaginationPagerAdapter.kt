package org.ladlb.directassemblee.core.widget

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter

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

abstract class PaginationPagerAdapter<T>(fragmentManager: FragmentManager, items: MutableList<T>) : PlaceholderPagerAdapter<T>(fragmentManager, items) {

    private var isShowingLoading = true

    private var isLoading = false

    var loadingMoreListener: LoadingMoreListener? = null

    override fun getItem(position: Int): Fragment {

        return if (isShowingLoading && (position == 0 || position >= getItemsSize())) {
            loadingMoreListener?.onLoadMore()
            LoadingFragment.newInstance()
        } else {
            super.getItem(position)
        }

    }

    override fun getCount(): Int = if (isShowingLoading) getItemsSize() + 1 else super.getCount()

    override fun addItems(i: List<T>) {
        super.addItems(i)
        isLoading = false
    }

    fun showLoading(show: Boolean) {
        isShowingLoading = show
        notifyDataSetChanged()
    }

    override fun clear() {
        showLoading(true)
        super.clear()
    }

    override fun getItemPosition(item: Any): Int {
        return when (item) {
            is LoadingFragment -> PagerAdapter.POSITION_NONE
            else -> PagerAdapter.POSITION_UNCHANGED
        }
    }

    interface LoadingMoreListener {

        fun onLoadMore()

    }

}
