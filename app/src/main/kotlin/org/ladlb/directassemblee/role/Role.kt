package org.ladlb.directassemblee.role

import android.os.Parcel
import android.os.Parcelable
import org.ladlb.directassemblee.helper.parcelableCreator
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

data class Role(val instanceType: String, val positions: Array<RolePosition> = arrayOf()) : Parcelable {

    /**
     * Constructor.
     *
     * @param parcel the source.
     */
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.createTypedArray(RolePosition.CREATOR)!!)

    /**
     * {@inheritDoc}
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role

        if (instanceType != other.instanceType) return false
        if (!Arrays.equals(positions, other.positions)) return false

        return true
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        var result = instanceType.hashCode()
        result = 31 * result + Arrays.hashCode(positions)
        return result
    }

    /**
     * The creator.
     */
    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Role)
    }

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(instanceType)
        parcel.writeTypedArray(positions, flags)
    }

    /**
     * {@inheritDoc}
     */
    override fun describeContents(): Int {
        return 0
    }
}