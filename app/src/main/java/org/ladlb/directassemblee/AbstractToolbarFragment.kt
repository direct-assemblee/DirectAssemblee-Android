package org.ladlb.directassemblee

import android.content.Context
import android.support.v7.widget.Toolbar

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

abstract class AbstractToolbarFragment : AbstractFragment() {

    private var mAbstractActivity: AbstractToolBarActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mAbstractActivity = activity as AbstractToolBarActivity
    }

    override fun onDetach() {
        super.onDetach()
        mAbstractActivity = null
    }

    val toolBar: Toolbar?
        get() = mAbstractActivity!!.toolbar

}
