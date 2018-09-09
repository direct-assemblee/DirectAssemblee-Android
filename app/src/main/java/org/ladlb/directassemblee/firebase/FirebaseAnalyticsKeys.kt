package org.ladlb.directassemblee.firebase

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
            val PARLIAMENT_GROUP: String = "parliament_group"
            val DISTRICT: String = "district"
        }

    }

    class Event {

        companion object {
            val SEARCH_DEPUTY_GEOLOCATION: String = "search_deputy_geolocation"
            val ADDRESS_SELECTED: String = "address_selected"
            val BACK_FROM_SELECT_ADDRESS: String = "back_from_select_address"
            val CONFIRM_CHANGE_DEPUTY: String = "confirm_change_deputy"
            val DEPUTY_FOUND: String = "deputy_found"
            val MULTIPLE_DEPUTIES_FOUND: String = "multiple_deputies_found"
            val DISPLAY_DEPUTY_DECLARATION: String = "display_deputy_declaration"
            val DENY_CHANGE_DEPUTY: String = "deny_change_deputy"
            val CALL_DEPUTY: String = "call_deputy"
            val SHARE: String = "share"
            val SEND_EMAIL_DEPUTY: String = "send_email_deputy"
            val NOTIFICATIONS_ENABLE: String = "notifications_enable"
            val DEPUTY_TIMELINE: String = "deputy_timeline"
            val DEPUTY_PROFILE: String = "deputy_profile"
            val DISPLAY_TIMELINE_EVENT_DETAIL: String = "display_timeline_event_detail"
            val DEPUTY_TIMELINE_LOAD_MORE: String = "deputy_timeline_load_more"
            val CANCEL_SEARCH_DEPUTY = "cancel_search_deputy"
            val SEARCH_DEPUTY_IN_LIST = "search_deputy_in_list"
        }

    }

    class ItemKey {

        companion object {
            val DEPUTY_ID: String = "deputy_id"
            val NAMES: String = "complete_name"
            val DISTRICT: String = "district"
            val PARLIAMENT_GROUP: String = "parliament_group"
            val DECLARATION_URL: String = "declaration_url"

            val NUMBER: String = "number"
            val ENABLE: String = "enable"
            val PAGE: String = "page"

            val TIMELINE_EVENT_ID: String = "timeline_event_id"
            val TIMELINE_EVENT_TITLE: String = "timeline_event_title"
            val TIMELINE_EVENT_THEME: String = "timeline_event_theme"
            val TIMELINE_EVENT_DATE: String = "timeline_event_date"
            val TIMELINE_EVENT_IS_ADOPTED: String = "timeline_event_is_adopted"
        }

    }

}
