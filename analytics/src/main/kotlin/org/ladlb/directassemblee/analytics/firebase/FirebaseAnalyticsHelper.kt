package org.ladlb.directassemblee.analytics.firebase

import android.os.Bundle
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.ItemKey
import org.ladlb.directassemblee.core.helper.FormatHelper
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.model.TimelineItem

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

class FirebaseAnalyticsHelper {

    companion object {

        fun addDeputy(bundle: Bundle, deputy: Deputy): Bundle {

            bundle.putInt(ItemKey.DEPUTY_ID, deputy.id)
            bundle.putString(ItemKey.NAMES, deputy.firstname + " " + deputy.lastname)
            bundle.putString(ItemKey.PARLIAMENT_GROUP, deputy.parliamentGroup)
            bundle.putString(ItemKey.DISTRICT, deputy.getCompleteLocality())

            return bundle

        }

        fun addTimeLineItem(bundle: Bundle, item: TimelineItem): Bundle {

            bundle.putInt(ItemKey.TIMELINE_EVENT_ID, item.id)
            bundle.putString(ItemKey.TIMELINE_EVENT_DATE, FormatHelper.format(item.date, FormatHelper.COMPACT))

            val title = item.title
            if (title != null) {
                bundle.putString(ItemKey.TIMELINE_EVENT_TITLE, title)
            }

            val theme = item.theme
            if (theme != null) {
                bundle.putString(ItemKey.TIMELINE_EVENT_THEME, theme.getLabel())
            }

            val info = item.extraBallotInfo
            if (info != null) {
                bundle.putBoolean(ItemKey.TIMELINE_EVENT_IS_ADOPTED, info.isAdopted)
            }

            return bundle

        }

    }

}