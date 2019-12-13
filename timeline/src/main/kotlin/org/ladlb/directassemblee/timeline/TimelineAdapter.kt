package org.ladlb.directassemblee.timeline

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.include_header_timeline_item.view.*
import kotlinx.android.synthetic.main.item_timeline.view.*
import org.ladlb.directassemblee.core.helper.DrawableHelper
import org.ladlb.directassemblee.core.helper.FormatHelper
import org.ladlb.directassemblee.core.helper.getColorPrimary
import org.ladlb.directassemblee.core.helper.getTextColorSecondary
import org.ladlb.directassemblee.core.widget.PaginationAdapter
import org.ladlb.directassemblee.model.TimelineItem

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

class TimelineAdapter(items: ArrayList<TimelineItem>) : PaginationAdapter<TimelineItem>(items) {

    companion object {
        const val typeItem: Int = 3
    }

    var listener: TimeLineAdapterListener? = null

    init {
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (getItemViewType(holder.adapterPosition)) {
            typeItem -> onBindItem(holder)
            else -> super.onBindViewHolder(holder, position)
        }

    }

    private fun onBindItem(holder: ViewHolder) {

        val holderPosition = holder.adapterPosition
        val item = getItemAtPosition(holderPosition)
        val view = holder.itemView
        val context = view.context

        view.tag = holderPosition
        view.setOnClickListener { v ->
            if (listener != null) {
                listener!!.onTimelineItemClicked(
                        v.tag as Int
                )
            }
        }

        view.textViewDate.text = FormatHelper.format(
                item.date,
                FormatHelper.COMPACT
        )
        view.textViewTitle.text = item.title

        val theme = item.theme
        val themeDrawableId: Int
        when (theme) {
            null -> {
                view.textViewTheme.visibility = View.INVISIBLE
                themeDrawableId = R.drawable.ic_uncategorized_24dp
            }
            else -> {
                view.textViewTheme.text = theme.getLabel()
                view.textViewTheme.visibility = if (TextUtils.isEmpty(view.textViewTheme.text)) View.GONE else View.VISIBLE
                themeDrawableId = theme.getDrawableId()
            }
        }

        view.imageViewTheme.setImageDrawable(
                DrawableHelper.getDrawableTintByColor(
                        view.resources,
                        themeDrawableId,
                        context.getColorPrimary()
                )
        )

        val description = item.description
        when {
            TextUtils.isEmpty(description) -> view.textViewSubTitle.visibility = View.GONE
            else -> {
                view.textViewSubTitle.visibility = View.VISIBLE
                view.textViewSubTitle.text = description
            }
        }

        when (val info = item.extraBallotInfo) {
            null -> view.linearLayoutVote.visibility = View.GONE
            else -> {
                val deputyVote = info.deputyVote

                view.linearLayoutVote.visibility = View.VISIBLE
                view.ballotVoteView.setBallotInfo(info)
                view.deputyVoteView.setDeputyInfo(deputyVote?.voteValue)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            when (viewType) {
                typeItem -> ItemHolder(
                        LayoutInflater.from(parent.context).inflate(
                                R.layout.item_timeline,
                                parent,
                                false
                        )
                )
                else -> super.onCreateViewHolder(parent, viewType)
            }

    override fun onCreatePlaceholderView(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = PlaceHolderHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_placeholder,
                        parent,
                        false
                )
        )
        // TODO : remove find view
        holder.itemView.findViewById<ImageView>(R.id.imageViewPlaceholder).setImageDrawable(
                DrawableHelper.getDrawableTintByColor(
                        holder.itemView.resources,
                        R.drawable.ic_empty_list_142dp,
                        holder.itemView.context.getTextColorSecondary()
                )
        )
        return holder
    }

    override fun onBindPlaceholderView(holder: ViewHolder, position: Int) {}

    override fun getItemViewType(position: Int): Int =
            if (position < getItemsSize()) typeItem else super.getItemViewType(position)

    class ItemHolder(view: View) : ViewHolder(view)

    class PlaceHolderHolder(view: View) : ViewHolder(view)

    override fun getItemId(position: Int): Long =
            if (position < getItemsSize()) getItemAtPosition(position).id.toLong() else super.getItemId(position)

    interface TimeLineAdapterListener {

        fun onTimelineItemClicked(itemPosition: Int)

    }

}
