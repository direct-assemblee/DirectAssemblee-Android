package org.ladlb.directassemblee

import android.os.Parcel
import android.os.Parcelable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

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

@RunWith(RobolectricTestRunner::class)
@Config(application = AssembleApplicationTest::class)
open class ParcelableTest<P : Parcelable> {

    protected var parcelable: P? = null

    @Test
    @Throws(Exception::class)
    fun testParcelable() {
        val parcel = Parcel.obtain()
        parcel.writeParcelable(parcelable, 0)
        parcel.setDataPosition(0)
        val readParcelable = parcel.readParcelable<Parcelable>(
                if (parcelable == null) null else parcelable!!::class.java.classLoader
        )
        assertEquals(parcelable, readParcelable)
    }

}