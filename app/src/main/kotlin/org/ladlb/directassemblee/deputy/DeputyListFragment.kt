package org.ladlb.directassemblee.deputy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.Nullable
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_deputy_list.*
import org.ladlb.directassemblee.AbstractFragment
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.DeputyAdapter.OnDeputyClickListener

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

class DeputyListFragment : AbstractFragment(), OnDeputyClickListener, Filterable {

    override fun getClassName(): String = "DeputyListFragment"

    private lateinit var adapter: DeputyAdapter

    private var listener: DeputyListFragmentListener? = null

    companion object {

        var TAG: String = DeputyListFragment::class.java.name

        private var ARG_DEPUTIES: String = "ARG_DEPUTIES"

        fun newInstance(deputies: List<Deputy>): DeputyListFragment {

            val bundle = Bundle()
            bundle.putParcelableArray(ARG_DEPUTIES, deputies.toTypedArray())

            val fragment = DeputyListFragment()
            fragment.arguments = bundle

            return fragment

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = activity as DeputyListFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement DeputyListFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_deputy_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        adapter = DeputyAdapter(
                (arguments!!.getParcelableArray(
                        ARG_DEPUTIES
                ) as Array<Deputy>).toMutableList()
        )
        adapter.setOnDeputyClickListener(this)

        val divider = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.divider_deputy, null)!!)

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(divider)

    }

    override fun onDeputyClicked(deputy: Deputy) {
        listener?.onDeputyClicked(deputy)
    }

    override fun getFilter(): Filter = adapter.filter

    interface DeputyListFragmentListener {

        fun onDeputyClicked(deputy: Deputy)

    }

}