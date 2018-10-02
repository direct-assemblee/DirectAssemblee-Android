package org.ladlb.directassemblee.address

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.item_address.view.*
import kotlinx.android.synthetic.main.item_placeholder.view.*
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.helper.ColorHelper
import org.ladlb.directassemblee.helper.DrawableHelper
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

class SearchAddressAdapter(items: ArrayList<Address>) : PlaceholderAdapter<Address>(items) {

    private val typeItem: Int = 3

    private val typeFooter: Int = 4

    var listener: OnAddressClickListener? = null

    private var query: String? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            when (viewType) {
                typeItem -> AddressViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                                R.layout.item_address,
                                parent,
                                false
                        )
                )
                typeFooter -> FooterViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                                R.layout.item_data_gouv,
                                parent,
                                false
                        )
                )
                else -> super.onCreateViewHolder(parent, viewType)
            }

    override fun onCreatePlaceholderView(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = PlaceholderHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_placeholder,
                        parent,
                        false
                )
        )
        holder.itemView.imageView.setImageDrawable(
                DrawableHelper.getDrawableTintByColor(
                        holder.itemView.resources,
                        R.drawable.ic_empty_place_142dp,
                        ColorHelper.getTextColorSecondary(holder.itemView.context)
                )
        )
        return holder
    }

    override fun onBindPlaceholderView(holder: ViewHolder, position: Int) {}

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(holder.adapterPosition)) {
            typeItem -> onBindItem(holder)
            else -> super.onBindViewHolder(holder, position)
        }
    }

    private fun onBindItem(holder: ViewHolder) {

        val view = holder.itemView
        val address = getItemAtPosition(holder.adapterPosition)

        view.tag = address
        view.setOnClickListener {
            listener?.onAddressClicked(it.tag as Address)
        }

        view.textViewName.text = address.properties?.name
        view.textViewContext.text = String.format(
                "%s %s",
                address.properties?.postcode,
                address.properties?.city
        )

    }

    override fun getItemId(position: Int): Long {
        return when {
            getItemsSize() == 0 -> super.getItemId(position)
            position < getItemsSize() -> getItemAtPosition(position).toString().hashCode().toLong()
            else -> Long.MAX_VALUE
        }
    }

    override fun getItemCount(): Int = if (getItemsSize() == 0) 1 else getItemsSize() + 1

    override fun getItemViewType(position: Int): Int {
        return if (position < getItemsSize()) {
            typeItem
        } else {
            if ((position < itemCount && getItemsSize() > 0) || (position == 0 && !isShowingPlaceHolder())) {
                typeFooter
            } else {
                super.getItemViewType(position)
            }
        }
    }

    fun setQuery(q: String?) {
        query = q
        notifyDataSetChanged()
    }

    class AddressViewHolder(view: View) : ViewHolder(view)

    class FooterViewHolder(view: View) : ViewHolder(view)

    class PlaceholderHolder(view: View) : ViewHolder(view)

    interface OnAddressClickListener {
        fun onAddressClicked(address: Address)
    }

}