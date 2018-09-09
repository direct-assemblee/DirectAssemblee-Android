package org.ladlb.directassemblee.declaration

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_declaration.view.*
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.declaration.DeclarationAdapter.DeclarationViewHolder
import org.ladlb.directassemblee.helper.FormatHelper

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

class DeclarationAdapter(private val context: Context) : Adapter<DeclarationViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private val items: ArrayList<Declaration> = arrayListOf()

    var listener: OnDeclarationClickListener? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeclarationViewHolder {
        return DeclarationViewHolder(
                inflater.inflate(
                        R.layout.item_declaration,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: DeclarationViewHolder, position: Int) {

        val declaration = items[holder.adapterPosition]

        holder.itemView.textViewTheme.text = declaration.title
        holder.itemView.textViewDate.text = FormatHelper.format(
                declaration.date,
                FormatHelper.DAY
        )

        holder.itemView.tag = declaration
        holder.itemView.setOnClickListener { view ->
            listener?.onDeclarationClicked(view.tag as Declaration)
        }

    }

    fun getItemOffsets(childAdapterPosition: Int, outRect: Rect) {

        val verticalMargin = context.resources.getDimensionPixelOffset(R.dimen.small_vertical_space)
        val horizontalMargin = context.resources.getDimensionPixelOffset(R.dimen.horizontal_space)

        outRect.top = if (childAdapterPosition == 0) verticalMargin else 0
        outRect.left = horizontalMargin
        outRect.right = horizontalMargin
        outRect.bottom = if (childAdapterPosition == itemCount - 1) verticalMargin else 0

    }

    fun addItems(i: Array<Declaration>) {
        items.addAll(i)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long = items[position].hashCode().toLong()

    class DeclarationViewHolder(view: View) : ViewHolder(view)

    interface OnDeclarationClickListener {
        fun onDeclarationClicked(declaration: Declaration)
    }

}
