package org.ladlb.directassemblee.helper

import android.util.Log
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.AnswersEvent
import com.crashlytics.android.answers.CustomEvent
import org.ladlb.directassemblee.BuildConfig

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

class MetricHelper {

    companion object {

        private val TAG = MetricHelper::class.java.name

        fun track(throwable: Throwable) {
            track(throwable.toString())
        }

        fun track(event: String) {

            if (BuildConfig.LOG_ENABLED) {
                Log.d(TAG, event)
            }

            if (BuildConfig.CRASH_TRACKER_ENABLED) {

                val maxLength = if (event.length < AnswersEvent.MAX_STRING_LENGTH) event.length else AnswersEvent.MAX_STRING_LENGTH - 1
                Answers.getInstance().logCustom(
                        CustomEvent(
                                event.substring(0, maxLength)
                        )
                )

            }

        }

    }

}