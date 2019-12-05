package org.ladlb.directassemblee.widget

import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

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

abstract class PlaceholderPagerAdapter<T>(fragmentManager: FragmentManager, private val items: MutableList<T>) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var isShowingPlaceHolder = true

    abstract fun getPlaceholderItem(position: Int): Fragment

    override fun getItem(position: Int): Fragment = getPlaceholderItem(position)

    @CallSuper
    open fun addItems(i: List<T>) {
        items.addAll(i)
        notifyDataSetChanged()
    }

    @CallSuper
    open fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getCount(): Int = if (items.isEmpty()) if (isShowingPlaceHolder) 1 else 0 else items.size

    fun getItemsSize(): Int = items.size

    fun getItemAtPosition(position: Int): T? = if (position in 0 until getItemsSize()) items[position] else null

    fun getItems(): ArrayList<T> = ArrayList(items)

    override fun getItemPosition(item: Any) = POSITION_NONE

    fun showPlaceholder(show: Boolean) {
        isShowingPlaceHolder = show
        notifyDataSetChanged()
    }

    fun isShowingPlaceHolder(): Boolean = isShowingPlaceHolder

}