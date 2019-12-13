package org.ladlb.directassemblee.timeline

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_time_line.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.core.AbstractFragment
import org.ladlb.directassemblee.core.helper.collect
import org.ladlb.directassemblee.core.widget.PaginationAdapter.LoadingMoreListener
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.model.TimelineItem
import org.ladlb.directassemblee.timeline.TimelineAdapter.TimeLineAdapterListener
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

class TimelineFragment : AbstractFragment(), LoadingMoreListener, OnRefreshListener, TimeLineAdapterListener {

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

    private val timelineGetPresenter: TimelineGetPresenter by viewModel {
        parametersOf(arguments!!.getParcelable<Deputy>(ARG_DEPUTY)!!.id)
    }

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by inject()

    private var appBarLayout: AppBarLayout? = null

    private var position: Int = 0

    private lateinit var adapter: TimelineAdapter

    private lateinit var deputy: Deputy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = TimelineAdapter(arrayListOf())
        adapter.showPlaceholder(false)
        adapter.showLoading(false)
        adapter.loadingMoreListener = this
        adapter.listener = this
    }

    private var listener: DeputyTimeLineFragmentListener? = null

    override fun onAttach(context: Context) {
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

        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter

        swipeRefreshLayout.isRefreshing = true
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)

        deputy = arguments!!.getParcelable(ARG_DEPUTY)!!

        timelineGetPresenter.viewState.collect(this) {
            when (it) {
                is Init -> onTimelineInitialised(it.items)
                is Add -> onTimelineItemsReceived(it.items)
                is Error -> onGetTimelineRequestFailed()
            }
        }

        timelineGetPresenter.get()

    }

    fun setDeputy(deputy: Deputy) {
        val deputyId = this.deputy.id
        this.deputy = deputy
        if (deputyId != this.deputy.id) {
            //timelineGetPresenter.get(deputyId)
        }
    }

    private fun onTimelineInitialised(timelineItem: List<TimelineItem>) {
        when {
            timelineItem.isEmpty() -> showPlaceholderIfNeeded()
            else -> {
                adapter.clear()
                adapter.addItems(timelineItem)
                // At the beginning, the loader is hidden
                adapter.showLoading(true)
                swipeRefreshLayout.isRefreshing = false

                (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).scrollToPositionWithOffset(
                        position,
                        0
                )

            }
        }
    }

    private fun onTimelineItemsReceived(timelineItem: List<TimelineItem>) {
        when {
            timelineItem.isEmpty() -> showPlaceholderIfNeeded()
            else -> adapter.addItems(timelineItem)
        }
    }

    private fun onGetTimelineRequestFailed() {
        showPlaceholderIfNeeded()
    }

    private fun showPlaceholderIfNeeded() {
        adapter.showLoading(false)
        adapter.showPlaceholder(adapter.getItemsSize() == 0)
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onLoadMore() {

/*
        FIXME :
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

    override fun onRefresh() {
        timelineGetPresenter.reload()
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
                        position = intent!!.getIntExtra(
                                TimelinePagerActivity.EXTRA_POSITION,
                                0
                        )
                        timelineGetPresenter.get()
                    }
                }
        }

    }

    interface DeputyTimeLineFragmentListener {

        fun onRefreshTimeLine(deputy: Deputy)

    }

}
