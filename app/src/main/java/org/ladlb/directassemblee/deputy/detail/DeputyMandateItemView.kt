package org.ladlb.directassemblee.deputy.detail

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.item_deputy_mandate.view.*
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.mandate.MandateAdapter
import org.ladlb.directassemblee.mandate.MandateItemDecoration

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

class DeputyMandateItemView : androidx.cardview.widget.CardView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        inflate(
                context,
                R.layout.item_deputy_mandate,
                this
        )

        recyclerView.addItemDecoration(MandateItemDecoration(resources))
        recyclerView.isNestedScrollingEnabled = false
    }

    fun setDeputy(deputy: Deputy) {

        val mandates = deputy.otherCurrentMandates

        textViewMandateHeader.text = resources.getQuantityString(
                R.plurals.mandate_other,
                mandates.size,
                mandates.size
        )

        recyclerView.adapter = MandateAdapter(mandates.toCollection(ArrayList()))
        recyclerView.visibility = View.VISIBLE

    }

}