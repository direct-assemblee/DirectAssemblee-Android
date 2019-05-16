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
                  val parliamentAgeInMonths: Int = -1, val declarations: Array<Declaration> = arrayOf(),
                  val activityRate: Int = -1, val salary: Int = -1,
                  val parliamentGroup: String? = null, val seatNumber: Int = -1,
                  val age: Int = -1, val roles: Array<Role> = arrayOf(), val otherCurrentMandates: Array<String> = arrayOf()) : Parcelable {

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
            parcel.createTypedArray(Declaration.CREATOR)!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.createTypedArray(Role.CREATOR)!!,
            parcel.createStringArray()!!
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
        parcel.writeTypedArray<Declaration>(declarations, flags)
        parcel.writeInt(activityRate)
        parcel.writeInt(salary)
        parcel.writeString(parliamentGroup)
        parcel.writeInt(seatNumber)
        parcel.writeInt(age)
        parcel.writeTypedArray<Role>(roles, flags)
        parcel.writeStringArray(otherCurrentMandates)
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

        other as Deputy

        if (id != other.id) return false
        if (firstname != other.firstname) return false
        if (lastname != other.lastname) return false
        if (department != other.department) return false
        if (district != other.district) return false
        if (commission != other.commission) return false
        if (phone != other.phone) return false
        if (email != other.email) return false
        if (job != other.job) return false
        if (currentMandateStartDate != other.currentMandateStartDate) return false
        if (photoUrl != other.photoUrl) return false
        if (parliamentAgeInMonths != other.parliamentAgeInMonths) return false
        if (!Arrays.equals(declarations, other.declarations)) return false
        if (activityRate != other.activityRate) return false
        if (salary != other.salary) return false
        if (parliamentGroup != other.parliamentGroup) return false
        if (seatNumber != other.seatNumber) return false
        if (age != other.age) return false
        if (!Arrays.equals(roles, other.roles)) return false
        if (!Arrays.equals(otherCurrentMandates, other.otherCurrentMandates)) return false

        return true
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (firstname?.hashCode() ?: 0)
        result = 31 * result + (lastname?.hashCode() ?: 0)
        result = 31 * result + (department?.hashCode() ?: 0)
        result = 31 * result + district
        result = 31 * result + (commission?.hashCode() ?: 0)
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (job?.hashCode() ?: 0)
        result = 31 * result + (currentMandateStartDate?.hashCode() ?: 0)
        result = 31 * result + (photoUrl?.hashCode() ?: 0)
        result = 31 * result + parliamentAgeInMonths
        result = 31 * result + Arrays.hashCode(declarations)
        result = 31 * result + activityRate
        result = 31 * result + salary
        result = 31 * result + (parliamentGroup?.hashCode() ?: 0)
        result = 31 * result + seatNumber
        result = 31 * result + age
        result = 31 * result + Arrays.hashCode(roles)
        result = 31 * result + Arrays.hashCode(otherCurrentMandates)
        return result
    }

    /**
     * The creator.
     */
    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Deputy)
    }
}