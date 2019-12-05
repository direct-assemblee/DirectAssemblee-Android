package org.ladlb.directassemblee.ballot.vote

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.ladlb.directassemblee.deputy.Deputy
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

data class BallotVote(@SerializedName("for")
                      val voteFor: List<Deputy> = listOf(),
                      @SerializedName("against")
                      val voteAgainst: List<Deputy> = listOf(),
                      @SerializedName("missing")
                      val voteMissing: List<Deputy> = listOf(),
                      @SerializedName("nonVoting")
                      val voteNonVoting: List<Deputy> = listOf(),
                      @SerializedName("blank")
                      val voteBlank: List<Deputy> = listOf()) : Parcelable {

    /**
     * Constructor.
     *
     * @param parcel the source.
     */
    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(Deputy.CREATOR)!!.toList(),
            parcel.createTypedArrayList(Deputy.CREATOR)!!.toList(),
            parcel.createTypedArrayList(Deputy.CREATOR)!!.toList(),
            parcel.createTypedArrayList(Deputy.CREATOR)!!.toList(),
            parcel.createTypedArrayList(Deputy.CREATOR)!!.toList()
    )

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(voteFor)
        parcel.writeTypedList(voteAgainst)
        parcel.writeTypedList(voteMissing)
        parcel.writeTypedList(voteNonVoting)
        parcel.writeTypedList(voteBlank)
    }

    /**
     * {@inheritDoc}
     */
    override fun describeContents(): Int = 0

    /**
     * The creator.
     */
    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = parcelableCreator(::BallotVote)
    }

}