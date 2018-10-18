package org.ladlb.directassemblee.deputy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.fragment_deputy.*
import kotlinx.android.synthetic.main.fragment_deputy_details.*
import kotlinx.android.synthetic.main.fragment_time_line.*
import kotlinx.android.synthetic.main.view_deputy_header.*
import org.ladlb.directassemblee.AbstractToolbarFragment
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.DeputyGetPresenter.DeputyGetView
import org.ladlb.directassemblee.deputy.detail.DeputyDetailsFragment
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.helper.ViewHelper
import org.ladlb.directassemblee.timeline.TimelineFragment
import org.ladlb.directassemblee.widget.DeputyToolbar

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

class DeputyFragment : AbstractToolbarFragment(), OnOffsetChangedListener, OnTabSelectedListener, DeputyGetView {

    override fun getClassName(): String = "DeputyFragment"

    private var isAvatarShown = true
    private var isTextShown = true

    private lateinit var deputyGetPresenter: DeputyGetPresenter

    private var adapter: DeputyPagerAdapter? = null

    private var toolbar: DeputyToolbar? = null

    companion object {

        val TAG: String = DeputyFragment::class.java.name

        private const val ARG_DEPUTY: String = "ARG_DEPUTY"

        private const val ANIMATION_DURATION = 200L
        private const val PERCENTAGE_TO_ANIMATE_AVATAR = 0.03
        private const val PERCENTAGE_TO_ANIMATE_TEXT = 0.5

        fun newInstance(deputy: Deputy): DeputyFragment {

            val bundle = Bundle()
            bundle.putParcelable(ARG_DEPUTY, deputy)

            val fragment = DeputyFragment()
            fragment.arguments = bundle

            return fragment

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deputyGetPresenter = DeputyGetPresenter(
                this,
                lifecycle
        )
    }

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_deputy, container, false)
    }

    private lateinit var deputy: Deputy

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appBarLayout.addOnOffsetChangedListener(this)

        val bar = view.rootView.findViewById<View>(R.id.toolbar)
        if (bar is DeputyToolbar) {
            toolbar = bar
        }

        deputy = arguments!!.getParcelable(ARG_DEPUTY)!!

        toolbar?.setDeputy(deputy)
        deputyCardView.setDeputy(deputy)

        adapter = DeputyPagerAdapter(
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
                getApiServices(),
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

        toolbar?.setDeputy(deputy)
        deputyCardView.setDeputy(deputy)

        for (fragment in childFragmentManager.fragments) {
            if (fragment is DeputyDetailsFragment) {
                fragment.setDeputy(this.deputy)
            } else if (fragment is TimelineFragment) {
                fragment.setDeputy(this.deputy)
            }
        }

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {

        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(verticalOffset).toFloat() / maxScroll.toFloat()

        updateAvatarVisibility(percentage)

    }

    private fun updateAvatarVisibility(percentage: Float) {

        val imageViewDeputy = toolbar?.findViewById<View>(R.id.imageViewDeputy)
        val textViewTitle = toolbar?.findViewById<TextView>(R.id.textViewTitle)
        val textViewSubTitle = toolbar?.findViewById<TextView>(R.id.textViewSubTitle)

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && isAvatarShown) {
            isAvatarShown = false

            ViewHelper.hideView(imageView, ANIMATION_DURATION)
            ViewHelper.showView(imageViewDeputy, ANIMATION_DURATION)

        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !isAvatarShown) {
            isAvatarShown = true

            ViewHelper.hideView(imageViewDeputy, ANIMATION_DURATION)
            ViewHelper.showView(imageView, ANIMATION_DURATION)

        }

        if (percentage >= PERCENTAGE_TO_ANIMATE_TEXT && isTextShown) {
            isTextShown = false

            ViewHelper.changeTextAlpha(textViewTitle, 1F, ANIMATION_DURATION)
            ViewHelper.changeTextAlpha(textViewSubTitle, 0.5F, ANIMATION_DURATION)

            ViewHelper.changeTextAlpha(textViewNames, 0F, ANIMATION_DURATION)
            ViewHelper.changeTextAlpha(textViewGroup, 0F, ANIMATION_DURATION)
            ViewHelper.changeTextAlpha(textViewDeputyPlace, 0F, ANIMATION_DURATION)

        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_TEXT && !isTextShown) {
            isTextShown = true

            ViewHelper.changeTextAlpha(textViewTitle, 0F, ANIMATION_DURATION)
            ViewHelper.changeTextAlpha(textViewSubTitle, 0F, ANIMATION_DURATION)

            ViewHelper.changeTextAlpha(textViewNames, 1F, ANIMATION_DURATION)
            ViewHelper.changeTextAlpha(textViewGroup, 1F, ANIMATION_DURATION)
            ViewHelper.changeTextAlpha(textViewDeputyPlace, 1F, ANIMATION_DURATION)

        }

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        when (viewPager.currentItem) {
            0 -> (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).scrollToPosition(0)
            1 -> nestedScrollView.scrollTo(0, 0)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tab?.position) {
            0 -> getFireBaseAnalytics().logEvent(
                    Event.DEPUTY_TIMELINE,
                    FirebaseAnalyticsHelper.addDeputy(
                            Bundle(),
                            deputy
                    )
            )
            1 -> getFireBaseAnalytics().logEvent(
                    Event.DEPUTY_PROFILE,
                    FirebaseAnalyticsHelper.addDeputy(
                            Bundle(),
                            deputy
                    )
            )
        }
    }

    override fun onDeputyReceived(deputy: Deputy) {

        if (isPrimaryDeputy(deputy)) {
            getPreferences().saveDeputy(deputy)
        }

        onGetDeputyFinished(deputy)

    }

    private fun isPrimaryDeputy(deputy: Deputy): Boolean {
        val primaryDeputy = getPreferences().loadDeputy()
        return (primaryDeputy != null && primaryDeputy.id == deputy.id)
    }

    override fun onGetDeputyRequestFailed() {
        // TODO : Show error popup
    }

}