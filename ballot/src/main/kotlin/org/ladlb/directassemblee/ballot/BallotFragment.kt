package org.ladlb.directassemblee.ballot

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_ballot.*
import kotlinx.android.synthetic.main.include_header_timeline_item.*
import org.koin.android.ext.android.inject
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.core.AbstractFragment
import org.ladlb.directassemblee.core.helper.DrawableHelper
import org.ladlb.directassemblee.core.helper.FormatHelper
import org.ladlb.directassemblee.core.helper.getColorPrimary
import org.ladlb.directassemblee.model.BallotInfo
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.model.TimelineItem
import org.ladlb.directassemblee.model.Vote
import java.util.*

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

class BallotFragment : AbstractFragment(), OnChartValueSelectedListener, OnClickListener {

    override fun getClassName(): String = "BallotFragment"

    override fun getTagName(): String = getClassName() + " " + ballot.id

    private lateinit var deputy: Deputy

    private lateinit var ballot: TimelineItem

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by inject()

    companion object {

        private var ARG_DEPUTY: String = "ARG_DEPUTY"
        private var ARG_BALLOT: String = "ARG_BALLOT"

        fun newInstance(deputy: Deputy, ballot: TimelineItem): BallotFragment {

            val bundle = Bundle()
            bundle.putParcelable(ARG_DEPUTY, deputy)
            bundle.putParcelable(ARG_BALLOT, ballot)

            val fragment = BallotFragment()
            fragment.arguments = bundle

            return fragment

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_ballot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deputy = arguments!!.getParcelable(ARG_DEPUTY)!!
        ballot = arguments!!.getParcelable(ARG_BALLOT)!!

        // Used for accessibility information
        linearLayoutBallotHeader.isFocusable = true

        setBallot(ballot)
    }

    private fun computeChart(ballotInfo: BallotInfo) {

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, ballotInfo.yesVotes.toFloat()))
        entries.add(BarEntry(1f, ballotInfo.noVotes.toFloat()))
        entries.add(BarEntry(2f, ballotInfo.blankVotes.toFloat()))
        entries.add(BarEntry(3f, ballotInfo.nonVoting.toFloat()))
        entries.add(BarEntry(4f, ballotInfo.missing.toFloat()))

        val colors = intArrayOf(
                ContextCompat.getColor(context!!, Vote.FOR.colorId),
                ContextCompat.getColor(context!!, Vote.AGAINST.colorId),
                ContextCompat.getColor(context!!, Vote.BLANK.colorId),
                ContextCompat.getColor(context!!, Vote.NON_VOTING.colorId),
                ContextCompat.getColor(context!!, Vote.MISSING.colorId)
        )

        val set = BarDataSet(entries, "BarDataSet")
        set.setColors(*colors)

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.axisMinimum = -0.5f
        xAxis.axisMaximum = 4.5f
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (value.toInt()) {
                    0 -> getString(Vote.FOR.labelId)
                    1 -> getString(Vote.AGAINST.labelId)
                    2 -> getString(Vote.BLANK.labelId)
                    3 -> getString(Vote.NON_VOTING.labelId)
                    4 -> getString(Vote.MISSING.labelId)
                    else -> ""
                }
            }
        }

        val yAxisLeft = barChart.axisLeft
        yAxisLeft.axisMinimum = 0f

        val yAxisRight = barChart.axisRight
        yAxisRight.axisMinimum = 0f

        val description = Description()
        description.isEnabled = false
        barChart.description = description

        val data = BarData(set)
        data.setValueTextSize(15f)

        barChart.animateY(500)
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setDrawGridBackground(false)
        barChart.data = data
        barChart.extraBottomOffset = 5f
        barChart.setFitBars(true)
        barChart.isDragEnabled = false
        barChart.setScaleEnabled(false)
        barChart.isScaleXEnabled = false
        barChart.isScaleYEnabled = false
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.setOnChartValueSelectedListener(this)
        barChart.invalidate()

    }

    private fun setBallot(ballot: TimelineItem) {

        when (val ballotInfo = ballot.extraBallotInfo) {
            null -> barChart.visibility = View.GONE
            else -> {
                computeChart(ballotInfo)
                ballotVoteResult.setBallotInfo(ballotInfo)
            }
        }

        textViewDate.text = FormatHelper.format(
                ballot.date,
                FormatHelper.COMPACT
        )
        textViewTitle.text = ballot.title

        val theme = ballot.theme
        val themeDrawableId: Int
        when (theme) {
            null -> {
                textViewTheme.visibility = View.INVISIBLE
                themeDrawableId = R.drawable.ic_uncategorized_24dp
            }
            else -> {
                textViewTheme.text = theme.getLabel()
                textViewTheme.visibility = if (TextUtils.isEmpty(textViewTheme.text)) View.GONE else View.VISIBLE
                themeDrawableId = theme.getDrawableId()
            }
        }

        imageViewTheme.setImageDrawable(
                DrawableHelper.getDrawableTintByColor(
                        resources,
                        themeDrawableId,
                        context!!.getColorPrimary()
                )
        )

        textViewDescription.text = ballot.description

        textViewLearnMore.setOnClickListener(this)
        textViewLearnMore.setURL(ballot.fileUrl)

    }

    private fun startBallotVoteActivity(page: Int) {
        startActivity(
                BallotVoteActivity.getIntent(
                        context!!,
                        ballot.id,
                        page
                )
        )
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        startBallotVoteActivity(
                e.x.toInt()
        )
        barChart.highlightValues(emptyArray())
    }

    override fun onClick(v: View?) {
        firebaseAnalyticsManager.logEvent(
                FirebaseAnalyticsKeys.Event.DISPLAY_TIMELINE_EVENT_DETAIL,
                FirebaseAnalyticsHelper.addDeputy(
                        FirebaseAnalyticsHelper.addTimeLineItem(
                                Bundle(),
                                ballot
                        ),
                        deputy
                )
        )
    }

}
