package org.ladlb.directassemblee.deputy.detail

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import kotlinx.android.synthetic.main.item_deputy_role.view.*
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.role.RoleAdapter
import org.ladlb.directassemblee.role.RoleDecorator

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

class DeputyRoleItemView : CardView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        inflate(
                context,
                R.layout.item_deputy_role,
                this
        )

        recyclerView.addItemDecoration(RoleDecorator(resources))
        recyclerView.isNestedScrollingEnabled = false

    }

    fun setDeputy(deputy: Deputy) {

        var instancesNumber = 0
        val roles = deputy.roles

        for (role in roles) {
            for (positions in role.positions) {
                instancesNumber += positions.instances.size
                if (instancesNumber > 1) {
                    break
                }
            }
        }

        textViewRole.text = resources.getQuantityString(
                R.plurals.role,
                instancesNumber,
                instancesNumber
        )

        recyclerView.adapter = RoleAdapter(deputy.roles.toCollection(ArrayList()))

    }

}