package org.ladlb.directassemblee.ballot

import android.content.Context
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import org.ladlb.directassemblee.deputy.DeputyListFragment
import org.ladlb.directassemblee.model.BallotVote
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

// FIXME
class BallotVotePagerAdapter(private val context: Context, fragmentManager: FragmentManager, private val values: BallotVote) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> DeputyListFragment.newInstance(values.voteFor)
        1 -> DeputyListFragment.newInstance(values.voteAgainst)
        2 -> DeputyListFragment.newInstance(values.voteBlank)
        3 -> DeputyListFragment.newInstance(values.voteNonVoting)
        4 -> DeputyListFragment.newInstance(values.voteMissing)
        else -> throw IndexOutOfBoundsException()
    }

    override fun getCount(): Int = 5

    override fun getPageTitle(position: Int): CharSequence = getPageTitle(
            position,
            when (position) {
                0 -> values.voteFor.size
                1 -> values.voteAgainst.size
                2 -> values.voteBlank.size
                3 -> values.voteNonVoting.size
                4 -> values.voteMissing.size
                else -> 0
            }
    )

    fun getPageTitle(position: Int, count: Int): CharSequence = when (position) {
        0 -> getSpannableTitle(count, context.getString(Vote.FOR.labelId))
        1 -> getSpannableTitle(count, context.getString(Vote.AGAINST.labelId))
        2 -> getSpannableTitle(count, context.getString(Vote.BLANK.labelId))
        3 -> getSpannableTitle(count, context.getString(Vote.NON_VOTING.labelId))
        4 -> getSpannableTitle(count, context.getString(Vote.MISSING.labelId))
        else -> ""
    }

    private fun getSpannableTitle(count: Int, label: String): SpannableString {
        val styledString = SpannableString(
                String.format("%s\n%d", label, count)
        )
        styledString.setSpan(
                AbsoluteSizeSpan(context.resources.getDimensionPixelSize(R.dimen.tabBallotVoteTitleSize)),
                0,
                label.length,
                0
        )
        styledString.setSpan(
                AbsoluteSizeSpan(context.resources.getDimensionPixelSize(R.dimen.tabBallotVoteCountSize)),
                styledString.length - count.toString().length,
                styledString.length,
                0
        )
        return styledString
    }

}
