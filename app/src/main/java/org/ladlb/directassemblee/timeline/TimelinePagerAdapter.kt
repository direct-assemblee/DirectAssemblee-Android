package org.ladlb.directassemblee.timeline

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import org.ladlb.directassemblee.ballot.BallotFragment
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

class TimelinePagerAdapter(fragmentManager: FragmentManager, private val timelineItems: ArrayList<TimelineItem>) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {

        val timelineItem = timelineItems[position]
        val info = timelineItem.extraBallotInfo
        return if (info == null) {
            MotionFragment.newInstance(
                    timelineItem
            )
        } else {
            BallotFragment.newInstance(
                    timelineItem
            )
        }

    }

    override fun getCount(): Int = timelineItems.size

    fun getItemValue(position: Int): TimelineItem = timelineItems[position]
}