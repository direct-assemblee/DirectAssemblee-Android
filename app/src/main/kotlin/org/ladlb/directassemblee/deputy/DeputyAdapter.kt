package org.ladlb.directassemblee.deputy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.item_deputy.view.*
import kotlinx.android.synthetic.main.item_placeholder.view.*
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.DeputyFilter.DeputyFilterListener
import org.ladlb.directassemblee.helper.DrawableHelper
import org.ladlb.directassemblee.helper.getTextColorSecondary
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

class DeputyAdapter(items: ArrayList<Deputy>) : PlaceholderAdapter<Deputy>(items), Filterable, DeputyFilterListener {

    companion object {
        const val typeItem: Int = 2
    }

    private var listener: OnDeputyClickListener? = null

    private val filter: DeputyFilter

    init {
        setHasStableIds(true)

        filter = DeputyFilter(ArrayList(items), this)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            when (viewType) {
                typeItem -> DeputyViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                                R.layout.item_deputy,
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
                        R.drawable.ic_deputy_search_empty_142dp,
                        holder.itemView.context.getTextColorSecondary()
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
        val context = view.context
        val deputy = getItemAtPosition(holder.adapterPosition)

        view.tag = deputy
        view.setOnClickListener { v ->
            listener?.onDeputyClicked(v.tag as Deputy)
        }

        view.textViewDeputyName.text = String.format(
                "%s %s",
                deputy.firstname,
                deputy.lastname
        )
        view.textViewDeputyGroup.text = deputy.parliamentGroup
        view.textViewDeputyPlace.text = DeputyHelper.getFormattedLocality(context.resources, deputy)
        view.imageViewDeputy.setDeputyUrl(deputy.photoUrl)

    }

    fun setOnDeputyClickListener(listener: OnDeputyClickListener) {
        this.listener = listener
    }

    override fun getItemViewType(position: Int): Int =
            if (position < getItemsSize()) typeItem else super.getItemViewType(position)

    override fun getItemId(position: Int): Long = if (getItemsSize() == 0) super.getItemId(position) else getItemAtPosition(position).id.toLong()

    override fun getFilter(): Filter = filter

    override fun onDeputyFiltered(deputies: Array<Deputy>) {
        clear()
        addItems(deputies)
    }

    class DeputyViewHolder(view: View) : ViewHolder(view)

    class PlaceholderHolder(view: View) : ViewHolder(view)

    interface OnDeputyClickListener {
        fun onDeputyClicked(deputy: Deputy)
    }

}
