package org.ladlb.directassemblee.helper

import android.content.res.Resources
import androidx.annotation.StringDef
import org.ladlb.directassemblee.R
import java.text.SimpleDateFormat
import java.util.*

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

class FormatHelper {

    companion object {

        @Retention(AnnotationRetention.SOURCE)
        @StringDef(DAY, COMPACT)
        annotation class DateFormat

        const val DAY = "EEEE d MMMM yyyy"
        const val COMPACT = "dd/MM/yyyy"

        fun format(date: Date?, @DateFormat format: String): String =
                if (date == null) "" else SimpleDateFormat(format, Locale.getDefault()).format(date)

        fun convertMonths(resources: Resources, months: Int): String {

            val years = months / 12
            val convertedMonth = months % 12

            return if (years > 0) {
                if (convertedMonth == 0) {
                    getYears(resources, years)
                } else {
                    String.format(
                            "%s %s",
                            getYears(resources, years),
                            getMonths(resources, convertedMonth)
                    )
                }
            } else {
                getMonths(resources, convertedMonth)
            }

        }

        private fun getMonths(resources: Resources, months: Int): String =
                resources.getString(R.string.month, months)

        private fun getYears(resources: Resources, years: Int): String {
            return resources.getQuantityString(
                    R.plurals.years,
                    years,
                    years
            )
        }

    }

}
