package org.ladlb.directassemblee.analytics.firebase

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

class FirebaseAnalyticsKeys {

    class UserProperty {

        companion object {
            const val PARLIAMENT_GROUP: String = "parliament_group"
            const val DISTRICT: String = "district"
        }

    }

    class Event {

        companion object {
            const val SEARCH_DEPUTY_GEOLOCATION: String = "search_deputy_geolocation"
            const val ADDRESS_SELECTED: String = "address_selected"
            const val BACK_FROM_SELECT_ADDRESS: String = "back_from_select_address"
            const val CONFIRM_CHANGE_DEPUTY: String = "confirm_change_deputy"
            const val DEPUTY_FOUND: String = "deputy_found"
            const val MULTIPLE_DEPUTIES_FOUND: String = "multiple_deputies_found"
            const val DISPLAY_DEPUTY_DECLARATION: String = "display_deputy_declaration"
            const val DENY_CHANGE_DEPUTY: String = "deny_change_deputy"
            const val CALL_DEPUTY: String = "call_deputy"
            const val SHARE: String = "share"
            const val SEND_EMAIL_DEPUTY: String = "send_email_deputy"
            const val NOTIFICATIONS_ENABLE: String = "notifications_enable"
            const val DEPUTY_TIMELINE: String = "deputy_timeline"
            const val DEPUTY_PROFILE: String = "deputy_profile"
            const val DISPLAY_TIMELINE_EVENT_DETAIL: String = "display_timeline_event_detail"
            const val DEPUTY_TIMELINE_LOAD_MORE: String = "deputy_timeline_load_more"
            const val CANCEL_SEARCH_DEPUTY = "cancel_search_deputy"
            const val SEARCH_DEPUTY_IN_LIST = "search_deputy_in_list"
        }

    }

    class ItemKey {

        companion object {
            const val DEPUTY_ID: String = "deputy_id"
            const val NAMES: String = "complete_name"
            const val DISTRICT: String = "district"
            const val PARLIAMENT_GROUP: String = "parliament_group"
            const val DECLARATION_URL: String = "declaration_url"

            const val NUMBER: String = "number"
            const val ENABLE: String = "enable"
            const val PAGE: String = "page"

            const val TIMELINE_EVENT_ID: String = "timeline_event_id"
            const val TIMELINE_EVENT_TITLE: String = "timeline_event_title"
            const val TIMELINE_EVENT_THEME: String = "timeline_event_theme"
            const val TIMELINE_EVENT_DATE: String = "timeline_event_date"
            const val TIMELINE_EVENT_IS_ADOPTED: String = "timeline_event_is_adopted"
        }

    }

}
