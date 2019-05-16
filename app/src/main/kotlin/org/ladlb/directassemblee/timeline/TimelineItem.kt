package org.ladlb.directassemblee.timeline

import android.os.Parcel
import android.os.Parcelable
import org.ladlb.directassemblee.ballot.BallotInfo
import org.ladlb.directassemblee.helper.parcelableCreator
import org.ladlb.directassemblee.theme.Theme
import java.util.*
import kotlin.collections.HashMap

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

data class TimelineItem(val id: Int = 0, val type: String? = null, val date: Date? = null,
                        val title: String? = null, val theme: Theme? = null,
                        val description: String? = null, val extraBallotInfo: BallotInfo? = null,
                        val extraInfos: Map<String, String>? = null,
                        val fileUrl: String? = null) : Parcelable {

    /**
     * The creator.
     */
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = parcelableCreator(::TimelineItem)
    }

    /**
     * {@inheritDoc}
     */
    override fun describeContents() = 0

    /**
     * Constructor.
     *
     * @param source the source.
     */
    @Suppress("UNCHECKED_CAST")
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            Date(source.readLong()),
            source.readString(),
            source.readParcelable(Theme::class.java.classLoader),
            source.readString(),
            source.readParcelable(BallotInfo::class.java.classLoader),
            source.readHashMap(HashMap::class.java.classLoader) as Map<String, String>?,
            source.readString()
    )

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(type)
        dest.writeLong(date?.time ?: 0)
        dest.writeString(title)
        dest.writeParcelable(theme, flags)
        dest.writeString(description)
        dest.writeParcelable(extraBallotInfo, flags)
        dest.writeMap(extraInfos)
        dest.writeString(fileUrl)
    }

}
