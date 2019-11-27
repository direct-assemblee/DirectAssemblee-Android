package org.ladlb.directassemblee.ballot

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

data class Ballot(val id: Int = 0, val date: Date? = null, val description: String? = null, val title: String? = null, val type: String? = null, val extraBallotInfo: BallotInfo? = null) : Parcelable {

    /**
     * Constructor.
     *
     * @param parcel the source.
     */
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            Date(parcel.readLong()),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(BallotInfo::class.java.classLoader))

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeLong(date?.time ?: 0)
        parcel.writeString(description)
        parcel.writeString(title)
        parcel.writeString(type)
        parcel.writeParcelable(extraBallotInfo, flags)
    }

    /**
     * The creator.
     */
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = parcelableCreator(::Ballot)
    }

    /**
     * {@inheritDoc}
     */
    override fun describeContents() = 0

}