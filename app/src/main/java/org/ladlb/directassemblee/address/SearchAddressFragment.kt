package org.ladlb.directassemblee.address

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_address_list.*
import org.ladlb.directassemblee.AbstractFragment
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

class SearchAddressFragment : AbstractFragment(), GetAddressPresenter.GetAddressPresenterView, SearchAddressAdapter.OnAddressClickListener {

    override fun getClassName(): String = "SearchAddressFragment"

    companion object {

        var TAG: String = SearchAddressFragment::class.java.name

        fun newInstance(): SearchAddressFragment = SearchAddressFragment()

    }

    private var listener: SearchAddressFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = activity as SearchAddressFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement SearchAddressFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_address_list, container, false)
    }

    private lateinit var adapter: SearchAddressAdapter

    private lateinit var getAddressPresenter: GetAddressPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAddressPresenter = GetAddressPresenter(
                this,
                lifecycle
        )

        adapter = SearchAddressAdapter(
                arrayListOf()
        )
        adapter.showPlaceholder(false)
        adapter.listener = this

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SearchAddressItemDecorator(context!!))

    }

    override fun onAddressesReceived(query: String, addresses: Array<Address>) {
        adapter.clear()

        if (addresses.isEmpty()) {
            adapter.showPlaceholder(true)
        } else {
            adapter.showPlaceholder(false)
            adapter.setQuery(query)
            adapter.addItems(addresses)
            recyclerView.scheduleLayoutAnimation();
        }

    }

    override fun onGetAddressRequestFailed() {
        adapter.showPlaceholder(true)
    }

    fun search(query: String?) {
        if (TextUtils.isEmpty(query)) {
            adapter.clear()
            adapter.showPlaceholder(false)
        } else {
            getAddressPresenter.get(getAddressServices(), query!!)
        }
    }

    override fun onAddressClicked(address: Address) {
        listener?.onAddressClicked(address)
    }

    interface SearchAddressFragmentListener {

        fun onAddressClicked(address: Address)

    }

}