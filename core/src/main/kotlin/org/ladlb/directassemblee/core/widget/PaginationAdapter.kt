package org.ladlb.directassemblee.core.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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

abstract class PaginationAdapter<T>(items: ArrayList<T>) : PlaceholderAdapter<T>(items) {

    private var isShowingLoading = true

    private var isLoading = false

    private val typeLoadingView = 2

    var loadingMoreListener: LoadingMoreListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            typeLoadingView -> LoadingHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_loading,
                            parent,
                            false
                    )
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (getItemViewType(holder.adapterPosition)) {
            typeLoadingView -> if (!isLoading) {
                isLoading = true
                loadingMoreListener?.onLoadMore()
            }
            else -> super.onBindViewHolder(holder, position)
        }

    }

    override fun getItemViewType(position: Int): Int = if (isShowingLoading && (position == 0 || position >= getItemsSize())) typeLoadingView else super.getItemViewType(position)

    override fun getItemCount(): Int = if (isShowingLoading) getItemsSize() + 1 else super.getItemCount()

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

    class LoadingHolder(view: View) : ViewHolder(view)

    interface LoadingMoreListener {

        fun onLoadMore()

    }

}
