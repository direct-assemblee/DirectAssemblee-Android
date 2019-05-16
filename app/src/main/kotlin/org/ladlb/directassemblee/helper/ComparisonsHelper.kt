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

class ComparisonsHelper {

    companion object {

        fun <T> compareBy(collator: Collator, vararg selectors: (T) -> Comparable<*>?): Comparator<T> {
            require(selectors.isNotEmpty())
            return Comparator { a, b -> compareValuesByImpl(collator, selectors, a, b) }
        }

        private fun <T> compareValuesByImpl(collator: Collator, selectors: Array<out (T) -> Comparable<*>?>, a: T, b: T): Int {
            for (fn in selectors) {
                val v1 = fn(a)
                val v2 = fn(b)
                val diff = compareValues(collator, v1, v2)
                if (diff != 0) return diff
            }
            return 0
        }

        private fun <T : Comparable<*>> compareValues(collator: Collator, a: T?, b: T?): Int {
            if (a === b) return 0
            if (a == null) return -1
            if (b == null) return 1

            @Suppress("UNCHECKED_CAST")
            return collator.compare((a as Comparable<Any>), b)
        }

    }

}