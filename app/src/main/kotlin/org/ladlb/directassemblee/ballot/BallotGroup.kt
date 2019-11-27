package org.ladlb.directassemblee.ballot

import android.os.Parcel
import android.os.Parcelable
import org.ladlb.directassemblee.helper.parcelableCreator
import org.ladlb.directassemblee.timeline.TimelineItem
import org.ladlb.directassemblee.timeline.TimelineItem.Companion.BALLOT_GROUP
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

data class BallotGroup(val contentDescription: String? = null, val lastBallotDate: Date? = null, val ballotsCount: Int = 0, val ballots: Array<Ballot> = arrayOf()) : TimelineItem, Parcelable {

    override fun getType(): Int = BALLOT_GROUP

    /**
     * Constructor.
     *
     * @param parcel the source.
     */
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            Date(parcel.readLong()),
            parcel.readInt(),
            parcel.createTypedArray(Ballot.CREATOR)!!)

    /**
     * {@inheritDoc}
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BallotGroup

        if (contentDescription != other.contentDescription) return false
        if (lastBallotDate != other.lastBallotDate) return false
        if (ballotsCount != other.ballotsCount) return false
        if (!ballots.contentEquals(other.ballots)) return false

        return true
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        var result = contentDescription?.hashCode() ?: 0
        result = 31 * result + (lastBallotDate?.hashCode() ?: 0)
        result = 31 * result + ballotsCount
        result = 31 * result + ballots.contentHashCode()
        return result
    }

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(contentDescription)
        parcel.writeLong(lastBallotDate?.time ?: 0)
        parcel.writeInt(ballotsCount)
        parcel.writeTypedArray(ballots, flags)
    }

    /**
     * The creator.
     */
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = parcelableCreator(::BallotGroup)
    }

    /**
     * {@inheritDoc}
     */
    override fun describeContents() = 0

}