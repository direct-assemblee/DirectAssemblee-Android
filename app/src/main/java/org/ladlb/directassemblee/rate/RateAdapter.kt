package org.ladlb.directassemblee.rate

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.item_placeholder.view.*
import kotlinx.android.synthetic.main.item_rate.view.*
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.helper.DrawableHelper
import org.ladlb.directassemblee.helper.getTextColorSecondary
import org.ladlb.directassemblee.mandate.MandateAdapter
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

class RateAdapter(items: ArrayList<Rate>) : PlaceholderAdapter<Rate>(items) {

    companion object {
        const val typeItem: Int = 2
    }

    init {
        setHasStableIds(true)
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
                        R.drawable.ic_empty_result_142dp,
                        holder.itemView.context.getTextColorSecondary()
                )
        )
        return holder
    }

    override fun onBindPlaceholderView(holder: ViewHolder, position: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            typeItem -> RateViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_rate,
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
            typeItem -> onBindItemViewHolder(holder as RateViewHolder, getItemAtPosition(adapterPosition))
            else -> super.onBindViewHolder(holder, position)
        }
    }

    private fun onBindItemViewHolder(holder: RateViewHolder, rate: Rate) {

        val context = holder.itemView.context
        holder.itemView.textViewGroupName.text = rate.group?.name
        holder.itemView.textViewPercent.text = context.getString(R.string.percent, rate.activityRate)

        val progressBar = holder.itemView.progressBar
        ViewCompat.setImportantForAccessibility(progressBar, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO)
        if (progressBar.progress == 0) {
            val animation = ObjectAnimator.ofInt(progressBar, "progress", rate.activityRate)
            animation.interpolator = AccelerateDecelerateInterpolator()
            animation.start()
        }

    }

    override fun getItemViewType(position: Int): Int =
            if (position < getItemsSize()) MandateAdapter.typeItem else super.getItemViewType(position)

    class RateViewHolder(view: View) : ViewHolder(view)

    class PlaceholderHolder(view: View) : ViewHolder(view)

}