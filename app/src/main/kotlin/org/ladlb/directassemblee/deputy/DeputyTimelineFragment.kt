package org.ladlb.directassemblee.deputy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.core.AbstractToolbarFragment
import org.ladlb.directassemblee.core.helper.ViewHelper
import org.ladlb.directassemblee.core.helper.collect
import org.ladlb.directassemblee.deputy.DeputyGetPresenter.DeputyGet.Error
import org.ladlb.directassemblee.deputy.DeputyGetPresenter.DeputyGet.Result
import org.ladlb.directassemblee.deputy.widget.DeputyToolbar
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.storage.PreferencesStorage
import org.ladlb.directassemblee.timeline.TimelineFragment
import kotlin.math.abs

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

class DeputyTimelineFragment : AbstractToolbarFragment(), OnOffsetChangedListener, OnTabSelectedListener {

    override fun getClassName(): String = "DeputyFragment"

    private lateinit var toolbar: DeputyToolbar

    private lateinit var imageViewDeputyHeader: View
    private lateinit var textViewDeputyHeaderNames: TextView
    private lateinit var textViewDeputyHeaderGroup: TextView
    private lateinit var textViewDeputyHeaderPlace: TextView

    private lateinit var imageViewToolbarDeputy: View
    private lateinit var textViewToolbarTitle: TextView
    private lateinit var textViewToolbarSubTitle: TextView

    private var isAvatarShown = true
    private var isTextShown = true

    private val deputyGetPresenter: DeputyGetPresenter by viewModel()

    private val preferenceStorage: PreferencesStorage by inject()

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by inject()

    private var adapter: DeputyTimelinePagerAdapter? = null

    companion object {

        val TAG: String = DeputyTimelineFragment::class.java.name

        private const val ARG_DEPUTY: String = "ARG_DEPUTY"

        private const val ANIMATION_DURATION = 200L
        private const val PERCENTAGE_TO_ANIMATE_AVATAR = 0.03
        private const val PERCENTAGE_TO_ANIMATE_TEXT = 0.5

        fun newInstance(deputy: Deputy): DeputyTimelineFragment {

            val bundle = Bundle()
            bundle.putParcelable(ARG_DEPUTY, deputy)

            val fragment = DeputyTimelineFragment()
            fragment.arguments = bundle

            return fragment

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deputyGetPresenter.viewState.collect(this) {
            when (it) {
                is Result -> onDeputyReceived(it.deputy)
                is Error -> onGetDeputyRequestFailed()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    private lateinit var deputy: Deputy

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.rootView.findViewById(R.id.toolbar)

        imageViewToolbarDeputy = toolbar.findViewById(R.id.imageViewToolbarDeputy)
        textViewToolbarTitle = toolbar.findViewById(R.id.textViewToolbarTitle)
        textViewToolbarSubTitle = toolbar.findViewById(R.id.textViewToolbarSubTitle)

        imageViewDeputyHeader = deputyHeaderView.findViewById(R.id.imageViewDeputyHeader)
        textViewDeputyHeaderNames = deputyHeaderView.findViewById(R.id.textViewDeputyHeaderNames)
        textViewDeputyHeaderGroup = deputyHeaderView.findViewById(R.id.textViewDeputyHeaderGroup)
        textViewDeputyHeaderPlace = deputyHeaderView.findViewById(R.id.textViewDeputyHeaderPlace)

        appBarLayout.addOnOffsetChangedListener(this)

        deputy = arguments!!.getParcelable(ARG_DEPUTY)!!

        toolbar.setDeputy(deputy)
        deputyHeaderView.setDeputy(deputy)

        adapter = DeputyTimelinePagerAdapter(
                context!!,
                childFragmentManager,
                deputy
        )

        viewPager.adapter = adapter

        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(this)

        updateContent()

    }

    private fun updateContent() {
        deputyGetPresenter.getDeputy(
                deputy.department!!.id,
                deputy.district
        )
    }

    fun setDeputy(deputy: Deputy) {
        this.deputy = deputy
        updateContent()
    }

    private fun onGetDeputyFinished(deputy: Deputy) {

        this.deputy = deputy

        toolbar.setDeputy(deputy)
        deputyHeaderView.setDeputy(deputy)

        for (fragment in childFragmentManager.fragments) {
            if (fragment is DeputyFragment) {
                fragment.setDeputy(this.deputy)
            } else if (fragment is TimelineFragment) {
                fragment.setDeputy(this.deputy)
            }
        }

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {

        val maxScroll = appBarLayout.totalScrollRange
        val percentage = abs(verticalOffset).toFloat() / maxScroll.toFloat()

        updateAvatarVisibility(percentage)

    }

    private fun updateAvatarVisibility(percentage: Float) {

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && isAvatarShown) {
            isAvatarShown = false

            ViewHelper.hideView(imageViewDeputyHeader, ANIMATION_DURATION)
            ViewHelper.showView(imageViewToolbarDeputy, ANIMATION_DURATION)
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !isAvatarShown) {
            isAvatarShown = true

            ViewHelper.hideView(imageViewToolbarDeputy, ANIMATION_DURATION)
            ViewHelper.showView(imageViewDeputyHeader, ANIMATION_DURATION)
        }

        if (percentage >= PERCENTAGE_TO_ANIMATE_TEXT && isTextShown) {
            isTextShown = false

            ViewHelper.changeTextAlpha(textViewToolbarTitle, 1F, ANIMATION_DURATION)
            ViewHelper.changeTextAlpha(textViewToolbarSubTitle, 0.5F, ANIMATION_DURATION)

            ViewHelper.changeTextAlpha(textViewDeputyHeaderNames, 0F, ANIMATION_DURATION)
            ViewHelper.changeTextAlpha(textViewDeputyHeaderGroup, 0F, ANIMATION_DURATION)
            ViewHelper.changeTextAlpha(textViewDeputyHeaderPlace, 0F, ANIMATION_DURATION)
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_TEXT && !isTextShown) {
            isTextShown = true

            ViewHelper.changeTextAlpha(textViewToolbarTitle, 0F, ANIMATION_DURATION)
            ViewHelper.changeTextAlpha(textViewToolbarSubTitle, 0F, ANIMATION_DURATION)

            ViewHelper.changeTextAlpha(textViewDeputyHeaderNames, 1F, ANIMATION_DURATION)
            ViewHelper.changeTextAlpha(textViewDeputyHeaderGroup, 1F, ANIMATION_DURATION)
            ViewHelper.changeTextAlpha(textViewDeputyHeaderPlace, 1F, ANIMATION_DURATION)
        }

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        when (viewPager.currentItem) {
            0 -> (view?.findViewById<RecyclerView>(R.id.recyclerView)?.layoutManager as LinearLayoutManager).scrollToPosition(0)
            1 -> view?.findViewById<NestedScrollView>(R.id.nestedScrollView)?.scrollTo(0, 0)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tab?.position) {
            0 -> firebaseAnalyticsManager.logEvent(
                    Event.DEPUTY_TIMELINE,
                    FirebaseAnalyticsHelper.addDeputy(
                            Bundle(),
                            deputy
                    )
            )
            1 -> firebaseAnalyticsManager.logEvent(
                    Event.DEPUTY_PROFILE,
                    FirebaseAnalyticsHelper.addDeputy(
                            Bundle(),
                            deputy
                    )
            )
        }
    }

    private fun onDeputyReceived(deputy: Deputy) {

        if (isPrimaryDeputy(deputy)) {
            preferenceStorage.saveDeputy(deputy)
        }

        onGetDeputyFinished(deputy)

    }

    private fun isPrimaryDeputy(deputy: Deputy): Boolean {
        val primaryDeputy = preferenceStorage.loadDeputy()
        return (primaryDeputy != null && primaryDeputy.id == deputy.id)
    }

    private fun onGetDeputyRequestFailed() {
        // TODO : Show error popup
    }

}
