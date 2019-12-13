package org.ladlb.directassemblee.timeline

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.ladlb.directassemblee.ballot.BallotFragment
import org.ladlb.directassemblee.core.widget.LoadingFragment
import org.ladlb.directassemblee.core.widget.PaginationPagerAdapter
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.model.TimelineItem
import org.ladlb.directassemblee.motion.MotionFragment

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

class TimelinePagerAdapter(fragmentManager: FragmentManager, private val deputy: Deputy, items: MutableList<TimelineItem>) : PaginationPagerAdapter<TimelineItem>(fragmentManager, items) {

    override fun getPlaceholderItem(position: Int) = LoadingFragment.newInstance()

    override fun getItem(position: Int): Fragment {

        val timelineItem = getItemAtPosition(position)
        return if (timelineItem == null) {
            super.getItem(position)
        } else {
            val info = timelineItem.extraBallotInfo
            if (info == null) {
                MotionFragment.newInstance(
                        deputy,
                        timelineItem
                )
            } else {
                BallotFragment.newInstance(
                        deputy,
                        timelineItem
                )
            }
        }

    }

}