package org.ladlb.directassemblee.deputy

import org.junit.Before
import org.ladlb.directassemblee.ParcelableTest
import org.ladlb.directassemblee.department.Department
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

class DeputyTest : ParcelableTest<Deputy>() {

    @Before
    @Throws(Exception::class)
    fun setUp() {
        parcelable = Deputy(
                firstname = "",
                lastname = "",
                department = Department(code = "", name = ""),
                commission = "",
                phone = "",
                email = "",
                job = "",
                currentMandateStartDate = Date(),
                photoUrl = "",
                declarations = arrayOf(),
                parliamentGroup = "",
                roles = arrayOf()
        )
    }

}