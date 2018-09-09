package org.ladlb.directassemblee.vote

import android.text.TextUtils
import org.ladlb.directassemblee.R

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

enum class Vote(val labelId: Int, val colorId: Int) {

    FOR(
            R.string.vote_result_for,
            R.color.green
    ),
    AGAINST(
            R.string.vote_result_against,
            R.color.red
    ),
    BLANK(
            R.string.vote_result_blank,
            R.color.hemicycle_empty
    ),
    MISSING(
            R.string.vote_result_missing,
            R.color.orange
    ),
    NON_VOTING(
            R.string.vote_result_non_voting,
            R.color.yellow
    ),
    SIGNED(
            R.string.timeline_event_motion_of_censure_signed,
            R.color.green
    ),
    NOT_SIGNED(
            R.string.timeline_event_motion_of_censure_not_signed,
            R.color.red
    );

    companion object {

        fun getVoteByValue(value: String?): Vote? {

            if (!TextUtils.isEmpty(value)) {
                when (value) {
                    "for" -> return FOR
                    "missing" -> return MISSING
                    "against" -> return AGAINST
                    "blank" -> return BLANK
                    "non-voting" -> return NON_VOTING
                    "signed" -> return SIGNED
                    "not_signed" -> return NOT_SIGNED
                }
            }
            return null

        }
    }

}