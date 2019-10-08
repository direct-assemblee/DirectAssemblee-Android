package org.ladlb.directassemblee.ballot

import android.os.Parcel
import android.os.Parcelable
import org.ladlb.directassemblee.deputy.DeputyVote
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

data class BallotInfo(val totalVotes: Int = 0, val yesVotes: Int = 0, val noVotes: Int = 0,
                      val nonVoting: Int = 0, val blankVotes: Int = 0, val missing: Int = 0,
                      val isAdopted: Boolean = false, val deputyVote: DeputyVote? = null) : Parcelable {

    /**
     * Constructor.
     *
     * @param source the source.
     */
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readByte() != 0.toByte(),
            source.readParcelable(DeputyVote::class.java.classLoader)
    )

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(totalVotes)
        parcel.writeInt(yesVotes)
        parcel.writeInt(noVotes)
        parcel.writeInt(nonVoting)
        parcel.writeInt(blankVotes)
        parcel.writeInt(missing)
        parcel.writeByte(if (isAdopted) 1 else 0)
        parcel.writeParcelable(deputyVote, flags)
    }

    /**
     * {@inheritDoc}
     */
    override fun describeContents(): Int = 0

    /**
     * The creator.
     */
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = parcelableCreator(::BallotInfo)
    }

}
