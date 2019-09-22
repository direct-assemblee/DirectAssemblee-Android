package org.ladlb.directassemblee.helper

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import org.ladlb.directassemblee.deputy.Deputy

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

class ComparisonHelperTest {

    @Test
    fun compareBy() {

        val deputiesResult = arrayOf(
                Deputy(firstname = "e", lastname = "e"),
                Deputy(firstname = "é", lastname = "e"),
                Deputy(firstname = "e", lastname = "é"),
                Deputy(firstname = "é", lastname = "é")
        )

        var deputies = arrayOf(
                Deputy(firstname = "é", lastname = "é"),
                Deputy(firstname = "é", lastname = "e"),
                Deputy(firstname = "e", lastname = "e"),
                Deputy(firstname = "e", lastname = "é")
        )

        deputies = deputies.sortedWith(
                ComparisonHelper.compareBy(
                        Deputy::lastname,
                        Deputy::firstname
                )
        ).toTypedArray()

        assertArrayEquals(deputies, deputiesResult)

    }

}