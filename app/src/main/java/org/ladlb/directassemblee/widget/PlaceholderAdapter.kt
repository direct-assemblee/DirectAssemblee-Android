package org.ladlb.directassemblee.widget

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

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

abstract class PlaceholderAdapter<T>(private val items: MutableList<T>) : Adapter<ViewHolder>() {

    private var isShowingPlaceHolder = true

    companion object {
        const val typePlaceHolder = 1
    }

    abstract fun onCreatePlaceholderView(parent: ViewGroup, viewType: Int): ViewHolder

    abstract fun onBindPlaceholderView(holder: ViewHolder, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = onCreatePlaceholderView(parent, viewType)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = onBindPlaceholderView(holder, position)

    @CallSuper
    open fun addItems(i: Array<T>) {
        items.addAll(i)
        notifyDataSetChanged()
    }

    @CallSuper
    open fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = if (position == 0 && items.size == 0 && isShowingPlaceHolder) typePlaceHolder else super.getItemViewType(position)

    override fun getItemCount(): Int = if (items.isEmpty()) if (isShowingPlaceHolder) 1 else 0 else items.size

    fun getItemsSize(): Int = items.size

    fun getItemAtPosition(position: Int): T = items[position]

    fun getItems(): ArrayList<T> = ArrayList(items)

    fun showPlaceholder(show: Boolean) {
        isShowingPlaceHolder = show
        notifyDataSetChanged()
    }

    fun isShowingPlaceHolder(): Boolean = isShowingPlaceHolder

}