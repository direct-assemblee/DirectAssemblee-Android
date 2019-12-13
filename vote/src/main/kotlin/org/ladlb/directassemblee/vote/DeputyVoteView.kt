package org.ladlb.directassemblee.vote

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.include_circle_stroke_image_view.view.*
import kotlinx.android.synthetic.main.view_vote.view.*
import org.ladlb.directassemblee.model.Vote

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

class DeputyVoteView : VoteView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setDeputyInfo(vote: Vote?) {

        if (vote == null) {
            visibility = View.GONE
        } else {
            val colorId = vote.colorId
            val colorResourceId = ContextCompat.getColor(context, colorId)

            imageViewCircle.setImageResource(R.drawable.ic_vote_person_40dp)
            imageViewCircle.setColorFilter(ContextCompat.getColor(context, colorId), android.graphics.PorterDuff.Mode.SRC_IN)

            ViewCompat.setBackgroundTintList(
                    imageViewCircle,
                    ResourcesCompat.getColorStateList(
                            context.resources,
                            colorId,
                            null
                    )
            )

            textViewVote.setTextColor(colorResourceId)
            textViewVote.setText(vote.labelId)
        }

    }

}
