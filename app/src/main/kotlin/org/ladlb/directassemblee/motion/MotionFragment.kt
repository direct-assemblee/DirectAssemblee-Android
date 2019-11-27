package org.ladlb.directassemblee.motion

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_motion.*
import kotlinx.android.synthetic.main.include_commission_name.*
import kotlinx.android.synthetic.main.include_commission_time.*
import kotlinx.android.synthetic.main.include_header_timeline_item.*
import kotlinx.android.synthetic.main.include_law_motives.*
import org.ladlb.directassemblee.AbstractFragment
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys
import org.ladlb.directassemblee.helper.DrawableHelper
import org.ladlb.directassemblee.helper.FormatHelper
import org.ladlb.directassemblee.helper.getColorPrimary
import org.ladlb.directassemblee.preferences.PreferencesStorageImpl
import org.ladlb.directassemblee.work.Work
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

class MotionFragment : AbstractFragment(), OnClickListener {

    override fun getTagName(): String = getClassName() + " " + timelineItem.id

    override fun getClassName(): String = "MotionFragment"

    @Inject
    lateinit var preferenceStorage: PreferencesStorageImpl

    private lateinit var timelineItem: Work

    companion object {

        val TAG: String = MotionFragment::class.java.name

        private const val LAW: String = "cosigned_law_proposal"
        private const val COMMISSION: String = "commission"
        private const val LAW_MOTIVES: String = "lawMotives"
        private const val COMMISSION_NAME: String = "commissionName"
        private const val COMMISSION_TIME: String = "commissionTime"

        private const val ARG_TIMELINE_ITEM: String = "ARG_TIMELINE_ITEM"

        fun newInstance(timelineItem: Work): MotionFragment {

            val bundle = Bundle()
            bundle.putParcelable(ARG_TIMELINE_ITEM, timelineItem)

            val fragment = MotionFragment()
            fragment.arguments = bundle

            return fragment

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_motion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Used for accessibility information
        linearLayoutMotionHeader.isFocusable = true

        val timeLineItem = arguments!!.getParcelable<Work>(ARG_TIMELINE_ITEM)!!

        setTimelineItem(timeLineItem)

    }

    private fun setTimelineItem(item: Work) {

        timelineItem = item

        textViewDate.text = FormatHelper.format(
                timelineItem.date,
                FormatHelper.COMPACT
        )
        textViewTitle.text = timelineItem.name

        val theme = timelineItem.theme
        val themeDrawableId: Int
        if (theme != null) {
            textViewTheme.text = theme.getLabel()
            textViewTheme.visibility = if (TextUtils.isEmpty(textViewTheme.text)) View.GONE else View.VISIBLE
            themeDrawableId = theme.getDrawableId()
        } else {
            textViewTheme.visibility = View.INVISIBLE
            themeDrawableId = R.drawable.ic_uncategorized_24dp
        }

        imageViewTheme.setImageDrawable(
                DrawableHelper.getDrawableTintByColor(
                        resources,
                        themeDrawableId,
                        context!!.getColorPrimary()
                )
        )

        val info = timelineItem.extraInfos
        if (info != null) {
            when (timelineItem.type) {
                LAW -> {
                    val lawMotives = info[LAW_MOTIVES]
                    if (!TextUtils.isEmpty(lawMotives)) {
                        stubLawMotives.inflate()
                        textViewLawMotives.text = lawMotives
                    }
                }
                COMMISSION -> {
                    val commissionName = info[COMMISSION_NAME]
                    if (!TextUtils.isEmpty(commissionName)) {
                        stubCommissionName.inflate()
                        textViewCommissionName.text = commissionName
                    }

                    val commissionTime = info[COMMISSION_TIME]
                    if (!TextUtils.isEmpty(commissionTime)) {
                        stubCommissionTime.inflate()
                        textViewCommissionTime.text = commissionTime
                    }
                }
            }
        }

        textViewDescription.text = timelineItem.description

        textViewLearnMore.setOnClickListener(this)
        textViewLearnMore.setURL(timelineItem.fileUrl)
    }

    override fun onClick(v: View?) {
        firebaseAnalyticsManager.logEvent(
                FirebaseAnalyticsKeys.Event.DISPLAY_TIMELINE_EVENT_DETAIL,
                FirebaseAnalyticsHelper.addDeputy(
                        FirebaseAnalyticsHelper.addTimeLineItem(
                                Bundle(),
                                timelineItem
                        ),
                        preferenceStorage.loadDeputy()!!
                )
        )
    }

}
