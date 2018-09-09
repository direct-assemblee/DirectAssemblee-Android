package org.ladlb.directassemblee.deputy

import android.text.TextUtils
import android.widget.Filter
import org.ladlb.directassemblee.helper.StringHelper

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

class DeputyFilter(private val items: List<Deputy>, private val listener: DeputyFilterListener) : Filter() {

    override fun performFiltering(constraint: CharSequence): FilterResults {

        val filterResults = Filter.FilterResults()
        val charString = constraint.toString()

        if (TextUtils.isEmpty(charString)) {
            filterResults.count = items.size
            filterResults.values = items.toTypedArray()
        } else {
            var firstname: String
            var lastname: String
            var parliamentGroup: String
            var departmentName: String
            var departmentCode: String
            val queryAccent = charString.toLowerCase().trim()
            val query = StringHelper.removeAccent(queryAccent)
            val queries = query.split("\\s+".toRegex())
            val filteredList: MutableList<Deputy> = mutableListOf()
            for (row in items) {

                var isValid = true

                firstname = StringHelper.removeAccent(row.firstname!!)
                lastname = StringHelper.removeAccent(row.lastname!!)
                parliamentGroup = StringHelper.removeAccent(row.parliamentGroup!!)
                departmentName = StringHelper.removeAccent(row.department!!.name!!)
                departmentCode = StringHelper.removeAccent(row.department.code!!)

                for (q in queries) {
                    if (!TextUtils.isEmpty(q)) {
                        val isInFirstName = firstname.contains(q, true)
                        val isInLastName = lastname.contains(q, true)
                        val isInParliamentGroup = parliamentGroup.contains(q, true)
                        val isInDepartmentName = departmentName.contains(q, true)
                        val isInDepartmentCode = departmentCode.contains(q, true)

                        if (!isInFirstName && !isInLastName && !isInParliamentGroup && !isInDepartmentName && !isInDepartmentCode) {
                            isValid = false
                            break
                        }
                    }
                }

                if (isValid) {
                    filteredList.add(row)
                }
            }

            filterResults.count = filteredList.size
            filterResults.values = filteredList.toTypedArray()
        }

        return filterResults

    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        @Suppress("UNCHECKED_CAST")
        listener.onDeputyFiltered(results.values as Array<Deputy>)
    }

    interface DeputyFilterListener {
        fun onDeputyFiltered(deputies: Array<Deputy>)
    }
}