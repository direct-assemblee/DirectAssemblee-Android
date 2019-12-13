package org.ladlb.directassemblee.timeline

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_timeline_pager.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.core.AbstractToolBarActivity
import org.ladlb.directassemblee.core.helper.collect
import org.ladlb.directassemblee.core.widget.PaginationPagerAdapter.LoadingMoreListener
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.model.TimelineItem
import org.ladlb.directassemblee.model.getFormattedLocality
import org.ladlb.directassemblee.timeline.TimelineGetPresenter.TimelineGet.Add
import org.ladlb.directassemblee.timeline.TimelineGetPresenter.TimelineGet.Init

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

class TimelinePagerActivity : AbstractToolBarActivity(), OnPageChangeListener, LoadingMoreListener {

    companion object Factory {

        var EXTRA_DEPUTY: String = "EXTRA_DEPUTY"

        var EXTRA_POSITION: String = "EXTRA_POSITION"

        fun getIntent(context: Context, deputy: Deputy, position: Int): Intent {

            val intent = Intent(context, TimelinePagerActivity::class.java)
            intent.putExtra(EXTRA_DEPUTY, deputy)
            intent.putExtra(EXTRA_POSITION, position)

            return intent

        }

    }

    private val timelineGetPresenter: TimelineGetPresenter by viewModel {
        parametersOf(intent.extras!!.getParcelable<Deputy>(EXTRA_DEPUTY)!!.id)
    }

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by inject()

    private lateinit var adapter: TimelinePagerAdapter

    private lateinit var deputy: Deputy

    private var position: Int = 0

    override fun getContentView(): Int = R.layout.activity_timeline_pager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras!!

        position = bundle.getInt(EXTRA_POSITION, 0)
        deputy = bundle.getParcelable(EXTRA_DEPUTY)!!

        adapter = TimelinePagerAdapter(
                supportFragmentManager,
                deputy,
                arrayListOf())
        adapter.showPlaceholder(false)
        adapter.showLoading(false)
        adapter.loadingMoreListener = this

        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(this)

        // TODO : remove find view
        Glide.with(this).load(deputy.photoUrl).into(findViewById(R.id.imageViewDeputy))

        // TODO : remove find view
        findViewById<TextView>(R.id.textViewDeputyName).text = String.format("%s %s", deputy.firstname, deputy.lastname)
        findViewById<TextView>(R.id.textViewDeputyGroup).text = deputy.parliamentGroup
        findViewById<TextView>(R.id.textViewDeputyPlace).text = deputy.getFormattedLocality(resources)

        timelineGetPresenter.viewState.collect(this) {
            when (it) {
                is Init -> onTimelineInitialised(it.items)
                is Add -> onTimelineItemsReceived(it.items)
                is Error -> onGetTimelineRequestFailed()
            }
        }

        timelineGetPresenter.get()

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        val timelineItem = adapter.getItemAtPosition(position)
        updateDeputyVote(timelineItem)
    }

    private fun updateDeputyVote(item: TimelineItem?) {

        val info = item?.extraBallotInfo
        if (info == null) {
            deputyVoteView.visibility = View.INVISIBLE
        } else {
            deputyVoteView.visibility = View.VISIBLE
            val deputyVote = info.deputyVote
            deputyVoteView.setDeputyInfo(deputyVote?.voteValue)
        }

    }

    override fun onBackPressed() {
        val data = Intent()
        data.putExtra(EXTRA_POSITION, viewPager.currentItem)
        setResult(Activity.RESULT_OK, data)
        super.onBackPressed()
    }

    override fun onLoadMore() {
/*
        firebaseAnalyticsManager.logEvent(
                Event.DEPUTY_TIMELINE_LOAD_MORE,
                FirebaseAnalyticsHelper.addDeputy(
                        Bundle().apply {
                            putInt(
                                    ItemKey.PAGE,
                                    page
                            )
                        },
                        deputy
                )
        )
*/

        timelineGetPresenter.loadMore()

    }

    private fun onTimelineInitialised(timelineItem: List<TimelineItem>) {
        when {
            timelineItem.isEmpty() -> showPlaceholderIfNeeded()
            else -> {
                adapter.clear()
                adapter.addItems(timelineItem)
                viewPager.setCurrentItem(position, false)
                onPageSelected(position)
            }
        }
    }

    private fun onTimelineItemsReceived(timelineItem: List<TimelineItem>) {
        when {
            timelineItem.isEmpty() -> showPlaceholderIfNeeded()
            else -> {
                adapter.addItems(timelineItem)
                adapter.showLoading(true)
                onPageSelected(viewPager.currentItem)
            }
        }

    }

    private fun onGetTimelineRequestFailed() {
        showPlaceholderIfNeeded()
    }

    private fun showPlaceholderIfNeeded() {
        adapter.showLoading(false)
        adapter.showPlaceholder(adapter.getItemsSize() == 0)
    }

}
