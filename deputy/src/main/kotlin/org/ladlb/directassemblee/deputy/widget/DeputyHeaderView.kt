package org.ladlb.directassemblee.deputy.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_deputy_header.view.*
import org.ladlb.directassemblee.deputy.R
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.model.getFormattedDistrict

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

class DeputyHeaderView : RelativeLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LinearLayout.inflate(
                context,
                R.layout.view_deputy_header,
                this
        )
    }

    fun setDeputy(deputy: Deputy) {

        imageViewDeputyHeader.setDeputyUrl(deputy.photoUrl)

        textViewDeputyHeaderNames.text = String.format("%s %s", deputy.firstname, deputy.lastname)

        textViewDeputyHeaderGroup.text = deputy.parliamentGroup

        var districtName = ""
        var departmentName = ""
        when {
            deputy.district > 0 -> districtName = deputy.getFormattedDistrict(resources)
        }

        val department = deputy.department
        when {
            department != null && !TextUtils.isEmpty(department.name) -> departmentName = department.name!!
        }

        when {
            !TextUtils.isEmpty(districtName) || !TextUtils.isEmpty(departmentName) -> textViewDeputyHeaderPlace.text = String.format(
                    "%s - %s",
                    districtName,
                    departmentName
            )
            else -> textViewDeputyHeaderPlace.text = if (TextUtils.isEmpty(districtName)) departmentName else districtName
        }

        textViewActivityValue.text = resources.getString(
                R.string.percent,
                deputy.activityRate
        )

    }

}
