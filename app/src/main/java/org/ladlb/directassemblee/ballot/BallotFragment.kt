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
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_ballot.*
import kotlinx.android.synthetic.main.include_header_timeline_item.*
import org.ladlb.directassemblee.AbstractFragment
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.ballot.vote.BallotVoteActivity
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys
import org.ladlb.directassemblee.helper.DrawableHelper
import org.ladlb.directassemblee.helper.FormatHelper
import org.ladlb.directassemblee.helper.getColorPrimary
import org.ladlb.directassemblee.timeline.TimelineItem
import org.ladlb.directassemblee.vote.Vote
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

class BallotFragment : AbstractFragment(), OnClickListener, OnChartValueSelectedListener {

    override fun getClassName(): String = "BallotFragment"

    override fun getTagName(): String = getClassName() + " " + ballot.id

    private lateinit var ballot: TimelineItem

    companion object {

        val TAG: String = BallotFragment::class.java.name

        private var ARG_BALLOT: String = "ARG_BALLOT"

        fun newInstance(ballot: TimelineItem): BallotFragment {

            val bundle = Bundle()
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

        // Used for accessibility information
        linearLayoutBallotHeader.isFocusable = true

        val ballot = arguments!!.getParcelable<TimelineItem>(ARG_BALLOT)!!

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
        xAxis.valueFormatter = IAxisValueFormatter { value, _ ->
            val position = value.toInt()

            when (position) {
                0 -> return@IAxisValueFormatter getString(Vote.FOR.labelId)
                1 -> return@IAxisValueFormatter getString(Vote.AGAINST.labelId)
                2 -> return@IAxisValueFormatter getString(Vote.BLANK.labelId)
                3 -> return@IAxisValueFormatter getString(Vote.NON_VOTING.labelId)
                4 -> return@IAxisValueFormatter getString(Vote.MISSING.labelId)
            }

            ""

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

    private fun setBallot(item: TimelineItem) {

        ballot = item

        val ballotInfo = ballot.extraBallotInfo
        when (ballotInfo) {
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
                        ballot,
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
        getFireBaseAnalytics().logEvent(
                FirebaseAnalyticsKeys.Event.DISPLAY_TIMELINE_EVENT_DETAIL,
                FirebaseAnalyticsHelper.addDeputy(
                        FirebaseAnalyticsHelper.addTimeLineItem(
                                Bundle(),
                                ballot
                        ),
                        getPreferences().loadDeputy()!!
                )
        )
    }

}
