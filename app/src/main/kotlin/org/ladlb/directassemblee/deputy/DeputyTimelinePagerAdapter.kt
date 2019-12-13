package org.ladlb.directassemblee.deputy

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.timeline.TimelineFragment

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

class DeputyTimelinePagerAdapter(private val context: Context, fragmentManager: FragmentManager, private val deputy: Deputy) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> TimelineFragment.newInstance(deputy)
        1 -> DeputyFragment.newInstance(deputy)
        else -> throw IndexOutOfBoundsException()
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> context.getString(R.string.activities)
        1 -> context.getString(R.string.profile)
        else -> ""
    }

}
