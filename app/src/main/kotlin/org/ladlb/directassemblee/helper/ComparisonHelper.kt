package org.ladlb.directassemblee.helper

import java.text.Collator
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

class ComparisonHelper {

    companion object {

        fun <T> compareBy(vararg selectors: (T) -> Comparable<*>?): Comparator<T> {
            require(selectors.isNotEmpty())
            return Comparator { a, b ->
                compareValuesByImpl(
                        Collator.getInstance(Locale.FRANCE).apply {
                            strength = Collator.SECONDARY
                        },
                        selectors,
                        a,
                        b
                )
            }
        }

        private fun <T> compareValuesByImpl(collator: Collator, selectors: Array<out (T) -> Comparable<*>?>, a: T, b: T): Int {
            for (fn in selectors) {
                val diff = compareValues(collator, fn(a), fn(b))
                if (diff != 0) return diff
            }
            return 0
        }

        private fun <T : Comparable<*>> compareValues(collator: Collator, a: T?, b: T?): Int {
            return when {
                (a === b) -> 0
                (a == null) -> -1
                (b == null) -> 1
                else -> collator.compare(a, b)
            }
        }

    }

}