package org.ladlb.directassemblee.role

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.widget.PlaceholderAdapter

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

class RoleAdapter(roles: ArrayList<Role>) : PlaceholderAdapter<Any>(computeItems(roles)) {

    companion object {

        const val typeRole = 2

        const val typeRolePosition = 3

        const val typeRoleInstance = 4

        // TODO : Async ?
        private fun computeItems(roles: List<Role>): MutableList<Any> {

            val items: MutableList<Any> = arrayListOf()

            for (role in roles) {
                items.add(role)
                for (position in role.positions) {
                    items.add(position)
                    items.addAll(position.instances)
                }
            }

            return items

        }

    }

    override fun onCreatePlaceholderView(parent: ViewGroup, viewType: Int): ViewHolder {
        return PlaceHolderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_content,
                        parent,
                        false
                )
        )
    }

    override fun onBindPlaceholderView(holder: ViewHolder, position: Int) {
        val view = holder.itemView
        (view as TextView).text = view.context.getString(R.string.deputy_details_no_roles)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            typeRole -> RoleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_title,
                            parent,
                            false
                    )
            )
            typeRolePosition -> RolePositionViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_subtitle,
                            parent,
                            false
                    )
            )
            typeRoleInstance -> RoleInstanceViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_content,
                            parent,
                            false
                    )
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val adapterPosition = holder.adapterPosition
        when (getItemViewType(adapterPosition)) {
            typeRole -> (holder.itemView as TextView).text = (getItemAtPosition(position) as Role).instanceType
            typeRolePosition -> (holder.itemView as TextView).text = (getItemAtPosition(position) as RolePosition).name
            typeRoleInstance -> (holder.itemView as TextView).text = (getItemAtPosition(position) as String)
            else -> super.onBindViewHolder(holder, position)
        }

    }

    override fun getItemViewType(position: Int): Int {

        return if (position < getItemsSize()) {
            when (getItemAtPosition(position)) {
                is Role -> typeRole
                is RolePosition -> typeRolePosition
                else -> typeRoleInstance
            }
        } else {
            super.getItemViewType(position)
        }

    }

    private class RoleViewHolder(itemView: View) : ViewHolder(itemView)

    private class RolePositionViewHolder(itemView: View) : ViewHolder(itemView)

    private class RoleInstanceViewHolder(itemView: View) : ViewHolder(itemView)

    private class PlaceHolderViewHolder(itemView: View) : ViewHolder(itemView)

}