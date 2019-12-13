package org.ladlb.directassemblee.model

import android.os.Parcel
import android.os.Parcelable
import org.ladlb.directassemblee.core.helper.parcelableCreator
import org.ladlb.directassemblee.core.helper.readEnum
import org.ladlb.directassemblee.core.helper.writeEnum

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

data class DeputyVote(val deputy: Deputy? = null, val voteValue: Vote? = null) : Parcelable {

    /**
     * Constructor.
     *
     * @param source the source.
     */
    constructor(source: Parcel) : this(
            source.readParcelable(Deputy::class.java.classLoader),
            source.readEnum<Vote>()
    )

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(deputy, flags)
        dest.writeEnum(voteValue)
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
        val CREATOR = parcelableCreator(::DeputyVote)
    }

}
