package org.ladlb.directassemblee.timeline

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_time_line.*
import org.ladlb.directassemblee.AbstractFragment
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.api.ladlb.RetrofitApiRepository
import org.ladlb.directassemblee.data.CacheManager
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.ItemKey
import org.ladlb.directassemblee.preferences.PreferencesStorageImpl
import org.ladlb.directassemblee.timeline.TimelineAdapter.TimeLineAdapterListener
import org.ladlb.directassemblee.timeline.TimelineGetPresenter.TimelineGetView
import org.ladlb.directassemblee.widget.PaginationAdapter.LoadingMoreListener
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

class TimelineFragment : AbstractFragment(), TimelineGetView, LoadingMoreListener, OnRefreshListener, TimeLineAdapterListener {

    override fun getClassName(): String = "TimelineFragment"

    private val requestTimeLinePager: Int = 0

    companion object {

        const val ARG_DEPUTY: String = "ARG_DEPUTY"

        fun newInstance(deputy: Deputy): TimelineFragment {

            val bundle = Bundle()
            bundle.putParcelable(ARG_DEPUTY, deputy)

            val fragment = TimelineFragment()
            fragment.arguments = bundle

            return fragment

        }

    }

    @Inject
    lateinit var timelineGetPresenter: TimelineGetPresenter

    @Inject
    lateinit var apiRepository: RetrofitApiRepository

    @Inject
    lateinit var cacheManager: CacheManager

    @Inject
    lateinit var preferenceStorage: PreferencesStorageImpl

    private var appBarLayout: AppBarLayout? = null

    private var page = 0

    private lateinit var adapter: TimelineAdapter

    private lateinit var deputy: Deputy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = TimelineAdapter(arrayListOf())
        adapter.showPlaceholder(false)
        adapter.loadingMoreListener = this
        adapter.listener = this

    }

    private var listener: DeputyTimeLineFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            listener = activity as DeputyTimeLineFragmentListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement DeputyTimeLineFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_time_line, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBar = view.rootView.findViewById<AppBarLayout>(R.id.appBarLayout)
        if (appBar is AppBarLayout) {
            appBarLayout = appBar
        }

        recyclerView.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(context!!, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)

        this.deputy = arguments!!.getParcelable(ARG_DEPUTY)!!
        loadTimeLine()

    }

    fun setDeputy(deputy: Deputy) {
        val deputyId = this.deputy.id
        this.deputy = deputy
        if (deputyId != this.deputy.id) {
            loadTimeLine()
        }
    }

    override fun onTimelineReceived(timelineItem: Array<TimelineItem>) {

        when {
            timelineItem.isEmpty() -> showPlaceholderIfNeeded()
            else -> {
                page++
                if (swipeRefreshLayout.isRefreshing) {
                    adapter.clear()
                }
                adapter.addItems(timelineItem)
                // At the beginning, the loader is hidden
                adapter.showLoading(true)
                swipeRefreshLayout.isRefreshing = false
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
        swipeRefreshLayout.isRefreshing = false
    }

    private fun loadTimeLine() {
        page = 0

        adapter.showLoading(false)

        swipeRefreshLayout.isRefreshing = true

        timelineGetPresenter.getTimeline(
                apiRepository,
                deputy.id,
                page
        )
    }

    override fun onLoadMore() {

        val bundle = Bundle()

        bundle.putInt(
                ItemKey.PAGE,
                page
        )

        firebaseAnalyticsManager.logEvent(
                Event.DEPUTY_TIMELINE_LOAD_MORE,
                FirebaseAnalyticsHelper.addDeputy(
                        bundle,
                        preferenceStorage.loadDeputy()!!
                )
        )

        timelineGetPresenter.getTimeline(
                apiRepository,
                deputy.id,
                page
        )

    }

    override fun onRefresh() {
        loadTimeLine()
        listener!!.onRefreshTimeLine(deputy)
    }

    override fun onTimelineItemClicked(itemPosition: Int) {

        val items = adapter.getItems()

        firebaseAnalyticsManager.logEvent(
                Event.DISPLAY_TIMELINE_EVENT_DETAIL,
                FirebaseAnalyticsHelper.addDeputy(
                        FirebaseAnalyticsHelper.addTimeLineItem(
                                Bundle(),
                                items[itemPosition]
                        ),
                        deputy
                )
        )

        cacheManager.put(
                CacheManager.timeLine,
                items
        )

        startActivityForResult(
                TimelinePagerActivity.getIntent(
                        context!!,
                        deputy,
                        itemPosition
                ),
                requestTimeLinePager
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        when (requestCode) {

            requestTimeLinePager ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        appBarLayout?.setExpanded(false)
                        (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).scrollToPositionWithOffset(
                                intent!!.getIntExtra(
                                        TimelinePagerActivity.EXTRA_POSITION,
                                        -1
                                ),
                                0
                        )
                    }
                }
        }

    }

    interface DeputyTimeLineFragmentListener {

        fun onRefreshTimeLine(deputy: Deputy)

    }

}
