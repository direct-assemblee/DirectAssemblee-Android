package org.ladlb.directassemblee.deputy

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import org.ladlb.directassemblee.declaration.Declaration
import org.ladlb.directassemblee.department.Department
import org.ladlb.directassemblee.helper.parcelableCreator
import org.ladlb.directassemblee.helper.readDate
import org.ladlb.directassemblee.helper.writeDate
import org.ladlb.directassemblee.role.Role
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

data class Deputy(val id: Int = -1, val firstname: String? = null,
                  val lastname: String? = null, val department: Department? = null,
                  val district: Int = -1, val commission: String? = null, val phone: String? = null,
                  val email: String? = null, val job: String? = null,
                  val currentMandateStartDate: Date? = null, val photoUrl: String? = null,
                  val parliamentAgeInMonths: Int = -1, val declarations: List<Declaration> = listOf(),
                  val activityRate: Int = -1, val salary: Int = -1,
                  val parliamentGroup: String? = null, val seatNumber: Int = -1,
                  val age: Int = -1, val roles: List<Role> = listOf(), val otherCurrentMandates: List<String> = listOf()) : Parcelable {

    /**
     * Constructor.
     *
     * @param parcel the source.
     */
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable<Department>(Department::class.java.classLoader),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDate(),
            parcel.readString(),
            parcel.readInt(),
            parcel.createTypedArray(Declaration.CREATOR)!!.toList(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.createTypedArray(Role.CREATOR)!!.toList(),
            parcel.createStringArray()!!.toList()
    )

    /**
     * Return a complete name for the deputy.
     *
     * @return Concatenation of firstname and lastname.
     */
    @NonNull
    fun getCompleteName(): String = String.format("%s %s", firstname, lastname)

    /**
     * Return the complete locality for the deputy.
     *
     * @return Concatenation of the district and the department code if it's not null.
     */
    @NonNull
    fun getCompleteLocality(): String = if (department == null) district.toString() else String.format(
            "%s-%s",
            department.code,
            district.toString()
    )

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(firstname)
        parcel.writeString(lastname)
        parcel.writeParcelable(department, flags)
        parcel.writeInt(district)
        parcel.writeString(commission)
        parcel.writeString(phone)
        parcel.writeString(email)
        parcel.writeString(job)
        parcel.writeDate(currentMandateStartDate)
        parcel.writeString(photoUrl)
        parcel.writeInt(parliamentAgeInMonths)
        parcel.writeTypedList(declarations)
        parcel.writeInt(activityRate)
        parcel.writeInt(salary)
        parcel.writeString(parliamentGroup)
        parcel.writeInt(seatNumber)
        parcel.writeInt(age)
        parcel.writeTypedList(roles)
        parcel.writeStringList(otherCurrentMandates)
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
        val CREATOR = parcelableCreator(::Deputy)
    }
}