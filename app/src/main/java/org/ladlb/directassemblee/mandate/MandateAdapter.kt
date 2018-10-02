package org.ladlb.directassemblee.mandate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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

class MandateAdapter(items: MutableList<String>) : PlaceholderAdapter<String>(items) {

    companion object {
        const val typeItem = 2
    }

    override fun onCreatePlaceholderView(parent: ViewGroup, viewType: Int): ViewHolder {
        return PlaceHolderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_content,
                        parent,
                        false
                )
        )
    }

    override fun onBindPlaceholderView(holder: ViewHolder, position: Int) {
        val view = holder.itemView
        (view as TextView).text = view.context.getString(R.string.deputy_details_no_other_current_mandate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            typeItem -> MandateViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_content,
                            parent,
                            false
                    )
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        when (getItemViewType(adapterPosition)) {
            typeItem -> (holder.itemView as TextView).text = (getItemAtPosition(position))
            else -> super.onBindViewHolder(holder, position)
        }
    }

    override fun getItemViewType(position: Int): Int =
            if (position < getItemsSize()) typeItem else super.getItemViewType(position)

    private class MandateViewHolder(itemView: View) : ViewHolder(itemView)

    private class PlaceHolderViewHolder(itemView: View) : ViewHolder(itemView)

}