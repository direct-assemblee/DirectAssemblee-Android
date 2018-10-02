package org.ladlb.directassemblee.deputy.detail

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.item_deputy_about.view.*
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.helper.FormatHelper

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

class DeputyAboutItemView : androidx.cardview.widget.CardView {

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
                R.layout.item_deputy_about,
                this
        )
    }

    fun setDeputy(deputy: Deputy) {

        textViewAbout.text = context.getString(
                R.string.deputy_details_about,
                deputy.firstname
        )

        // Job
        val job = deputy.job
        if (TextUtils.isEmpty(job)) {
            subViewJob.visibility = View.GONE
        } else {
            subViewJob.visibility = View.VISIBLE
            subViewJob.setContent(job)
        }

        // Age
        subViewAge.setContent(
                context.getString(
                        R.string.age,
                        deputy.age
                )
        )

        // ParliamentAge
        val parliamentAge = deputy.parliamentAgeInMonths
        if (parliamentAge == -1) {
            subViewParliamentAge.visibility = View.GONE
        } else {
            subViewParliamentAge.visibility = View.VISIBLE
            subViewParliamentAge.setContent(
                    FormatHelper.convertMonths(
                            resources,
                            parliamentAge
                    )
            )
        }

        // Mandate start
        val mandateStart = deputy.currentMandateStartDate
        if (mandateStart == null) {
            subViewMandateStart.visibility = View.GONE
        } else {
            subViewMandateStart.visibility = View.VISIBLE
            subViewMandateStart.setContent(
                    FormatHelper.format(
                            mandateStart,
                            FormatHelper.COMPACT
                    )
            )
        }

        // salary
        val salary = deputy.salary
        if (salary == -1) {
            subViewAmount.visibility = View.GONE
        } else {
            subViewAmount.visibility = View.VISIBLE
            subViewAmount.setContent(
                    context.getString(
                            R.string.amount,
                            salary
                    )
            )
        }

    }
}