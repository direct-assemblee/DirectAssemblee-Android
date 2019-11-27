package org.ladlb.directassemblee.work

import android.os.Parcel
import android.os.Parcelable
import org.ladlb.directassemblee.helper.parcelableCreator
import org.ladlb.directassemblee.theme.Theme
import org.ladlb.directassemblee.timeline.TimelineItem
import org.ladlb.directassemblee.timeline.TimelineItem.Companion.WORK
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

data class Work(val id: Int = 0, val name: String? = null, val date: Date? = null, val fileUrl: String? = null, val description: String? = null, val workType: WorkType? = null, val theme: Theme? = null, val extraInfos: Map<String, String>? = null) : TimelineItem, Parcelable {

    override fun getType(): Int = WORK

    /**
     * Constructor.
     *
     * @param parcel the source.
     */
    @Suppress("UNCHECKED_CAST")
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            Date(parcel.readLong()),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(WorkType::class.java.classLoader),
            parcel.readParcelable(Theme::class.java.classLoader),
            parcel.readHashMap(HashMap::class.java.classLoader) as Map<String, String>?)

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeLong(date?.time ?: 0)
        parcel.writeString(fileUrl)
        parcel.writeString(description)
        parcel.writeParcelable(workType, flags)
        parcel.writeParcelable(theme, flags)
        parcel.writeMap(extraInfos)
    }

    /**
     * The creator.
     */
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = parcelableCreator(::WorkType)
    }

    /**
     * {@inheritDoc}
     */
    override fun describeContents() = 0

}