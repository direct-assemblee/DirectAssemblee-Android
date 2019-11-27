package org.ladlb.directassemblee.law

import android.os.Parcel
import android.os.Parcelable
import org.ladlb.directassemblee.helper.parcelableCreator
import org.ladlb.directassemblee.theme.Theme
import org.ladlb.directassemblee.timeline.TimelineItem
import org.ladlb.directassemblee.timeline.TimelineItem.Companion.LAW
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

data class Law(val id: Int = 0, val name: String? = null, val contentDescription: String? = null, val lastBallotDate: Date? = null, val ballotsCount: Int = 0, val lawType: LawType? = null, val theme: Theme? = null) : TimelineItem, Parcelable {

    override fun getType(): Int = LAW

    /**
     * Constructor.
     *
     * @param parcel the source.
     */
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            Date(parcel.readLong()),
            parcel.readInt(),
            parcel.readParcelable(LawType::class.java.classLoader),
            parcel.readParcelable(Theme::class.java.classLoader))

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(contentDescription)
        parcel.writeLong(lastBallotDate?.time ?: 0)
        parcel.writeInt(ballotsCount)
        parcel.writeParcelable(lawType, flags)
        parcel.writeParcelable(theme, flags)
    }

    /**
     * The creator.
     */
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = parcelableCreator(::LawType)
    }

    /**
     * {@inheritDoc}
     */
    override fun describeContents() = 0

}