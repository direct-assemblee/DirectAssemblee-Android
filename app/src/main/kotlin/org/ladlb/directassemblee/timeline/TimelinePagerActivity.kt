package org.ladlb.directassemblee.timeline

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_timeline_pager.*
import kotlinx.android.synthetic.main.item_deputy.*
import org.ladlb.directassemblee.AbstractToolBarActivity
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.deputy.DeputyHelper
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.ItemKey
import org.ladlb.directassemblee.timeline.TimelineGetPresenter.TimelineGetView
import org.ladlb.directassemblee.widget.PaginationPagerAdapter.LoadingMoreListener
import javax.inject.Inject

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

class TimelinePagerActivity : AbstractToolBarActivity(), TimelineGetView, OnPageChangeListener, LoadingMoreListener {

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

    @Inject
    lateinit var timelineGetPresenter: TimelineGetPresenter

    @Inject
    lateinit var cacheManager: TimelineCacheManager

    private lateinit var adapter: TimelinePagerAdapter

    private lateinit var deputy: Deputy

    private var page: Int = 0

    override fun getContentView(): Int = R.layout.activity_timeline_pager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras!!
        val position = bundle.getInt(EXTRA_POSITION, 0)

        deputy = bundle.getParcelable(EXTRA_DEPUTY)!!

        adapter = TimelinePagerAdapter(
                supportFragmentManager,
                arrayListOf())
        adapter.showPlaceholder(false)
        adapter.loadingMoreListener = this

        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(this)

        Glide.with(this).load(deputy.photoUrl).into(imageViewDeputy)

        textViewDeputyName.text = String.format("%s %s", deputy.firstname, deputy.lastname)
        textViewDeputyGroup.text = deputy.parliamentGroup
        textViewDeputyPlace.text = DeputyHelper.getFormattedLocality(resources, deputy)

        loadTimeLine(deputy.id, position)

    }

    private fun loadTimeLine(deputyId: Int, position: Int) {

        val cache = cacheManager.getAll(deputyId)
        if (cache == null) {
            timelineGetPresenter.getTimeline(
                    deputyId,
                    page
            )
        } else {
            page = cache.first
            adapter.addItems(cache.second)

            viewPager.currentItem = position
            onPageSelected(if (position >= adapter.getItemsSize()) adapter.getItemsSize() - 1 else position)

        }

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

        timelineGetPresenter.getTimeline(
                deputy.id,
                page
        )

    }

    override fun onTimelineReceived(timelineItem: List<TimelineItem>) {

        when {
            timelineItem.isEmpty() -> showPlaceholderIfNeeded()
            else -> {
                page++
                adapter.addItems(timelineItem)
                // At the beginning, the loader is hidden
                adapter.showLoading(true)

                onPageSelected(viewPager.currentItem)
            }
        }

    }

    override fun onGetTimelineRequestFailed() {
        showPlaceholderIfNeeded()
    }

    private fun showPlaceholderIfNeeded() {
        adapter.canLoadMore = false
        adapter.showLoading(false)
        adapter.showPlaceholder(adapter.getItemsSize() == 0)
    }

}
