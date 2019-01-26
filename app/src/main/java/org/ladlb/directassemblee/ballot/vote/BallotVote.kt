package org.ladlb.directassemblee.ballot.vote

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.ladlb.directassemblee.deputy.Deputy
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

data class BallotVote(@SerializedName("for")
                      val voteFor: Array<Deputy> = emptyArray(),
                      @SerializedName("against")
                      val voteAgainst: Array<Deputy> = emptyArray(),
                      @SerializedName("missing")
                      val voteMissing: Array<Deputy> = emptyArray(),
                      @SerializedName("nonVoting")
                      val voteNonVoting: Array<Deputy> = emptyArray(),
                      @SerializedName("blank")
                      val voteBlank: Array<Deputy> = emptyArray()) : Parcelable {

    /**
     * Constructor.
     *
     * @param source the source.
     */
    constructor(source: Parcel) : this(
            source.createTypedArray(Deputy.CREATOR)!!,
            source.createTypedArray(Deputy.CREATOR)!!,
            source.createTypedArray(Deputy.CREATOR)!!,
            source.createTypedArray(Deputy.CREATOR)!!,
            source.createTypedArray(Deputy.CREATOR)!!
    )

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedArray<Deputy>(voteFor, flags)
        dest.writeTypedArray<Deputy>(voteAgainst, flags)
        dest.writeTypedArray<Deputy>(voteMissing, flags)
        dest.writeTypedArray<Deputy>(voteNonVoting, flags)
        dest.writeTypedArray<Deputy>(voteBlank, flags)
    }

    /**
     * {@inheritDoc}
     */
    override fun describeContents(): Int = 0

    /**
     * {@inheritDoc}
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BallotVote

        if (!Arrays.equals(voteFor, other.voteFor)) return false
        if (!Arrays.equals(voteAgainst, other.voteAgainst)) return false
        if (!Arrays.equals(voteMissing, other.voteMissing)) return false
        if (!Arrays.equals(voteNonVoting, other.voteNonVoting)) return false
        if (!Arrays.equals(voteBlank, other.voteBlank)) return false

        return true
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        var result = Arrays.hashCode(voteFor)
        result = 31 * result + Arrays.hashCode(voteAgainst)
        result = 31 * result + Arrays.hashCode(voteMissing)
        result = 31 * result + Arrays.hashCode(voteNonVoting)
        result = 31 * result + Arrays.hashCode(voteBlank)
        return result
    }

    /**
     * The creator.
     */
    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = parcelableCreator(::BallotVote)
    }

}