package org.ladlb.directassemblee.rate

import android.os.Parcel
import android.os.Parcelable
import org.ladlb.directassemblee.helper.parcelableCreator

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

data class RateGroup(val id: Int = -1, val name: String? = null) : Parcelable {

    /**
     * Constructor.
     *
     * @param parcel the source.
     */
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString())

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    /**
     * {@inheritDoc}
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * The creator.
     */
    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::RateGroup)
    }

}