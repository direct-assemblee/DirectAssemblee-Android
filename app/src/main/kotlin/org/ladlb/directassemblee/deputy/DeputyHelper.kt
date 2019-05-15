package org.ladlb.directassemblee.deputy

import android.content.res.Resources
import android.text.TextUtils
import org.ladlb.directassemblee.R

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

class DeputyHelper {

    companion object {

        fun getFormattedDistrict(resources: Resources, deputy: Deputy): String =
                resources.getString(
                        R.string.district,
                        resources.getQuantityString(
                                R.plurals.disctricts,
                                deputy.district,
                                deputy.district
                        )
                )

        fun getFormattedLocality(resources: Resources, deputy: Deputy): String {

            var departmentName = ""
            val districtName = getFormattedDistrict(resources, deputy)
            val department = deputy.department

            if (department != null && !TextUtils.isEmpty(department.name)) {
                departmentName = department.name!!
            }

            return if (!TextUtils.isEmpty(districtName) || !TextUtils.isEmpty(departmentName)) {
                String.format(
                        "%s - %s",
                        districtName,
                        departmentName
                )
            } else {
                if (TextUtils.isEmpty(districtName)) departmentName else districtName
            }

        }

    }

}
