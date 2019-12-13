package org.ladlb.directassemblee.model

import android.os.Parcel
import android.os.Parcelable
import org.ladlb.directassemblee.core.helper.parcelableCreator
import org.ladlb.directassemblee.core.helper.readDate
import org.ladlb.directassemblee.core.helper.writeDate
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

data class Declaration(val title: String? = null, val date: Date? = null, val url: String? = null) : Parcelable {

    /**
     * Constructor.
     *
     * @param source the source.
     */
    constructor(source: Parcel) : this(
            source.readString(),
            source.readDate(),
            source.readString()
    )

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeDate(date)
        parcel.writeString(url)
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
        val CREATOR = parcelableCreator(::Declaration)
    }

}
