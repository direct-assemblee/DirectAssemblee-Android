package org.ladlb.directassemblee.address

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import kotlinx.android.synthetic.main.fragment_address_list.*
import org.koin.android.ext.android.inject
import org.ladlb.directassemblee.address.AddressAdapter.OnAddressClickListener
import org.ladlb.directassemblee.address.AddressGetPresenter.AddressGet.Result
import org.ladlb.directassemblee.core.AbstractToolbarFragment
import org.ladlb.directassemblee.core.helper.collect

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

class AddressFragment : AbstractToolbarFragment(), OnAddressClickListener {

    override fun getClassName(): String = "SearchAddressFragment"

    companion object {

        var TAG: String = AddressFragment::class.java.name

        fun newInstance(): AddressFragment = AddressFragment()

    }

    private val addressGetPresenter: AddressGetPresenter by inject()

    private lateinit var adapter: AddressAdapter

    private var listener: AddressFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = activity as AddressFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement SearchAddressFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addressGetPresenter.viewState.collect(this) {
            when (it) {
                is Result -> onAddressesReceived(it.query, it.addresses)
                is Error -> onGetAddressRequestFailed()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_address_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AddressAdapter(
                arrayListOf()
        )
        adapter.showPlaceholder(false)
        adapter.listener = this

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(AddressItemDecoration(resources))

    }

    private fun onAddressesReceived(query: String, addresses: List<org.ladlb.directassemblee.model.Address>) {
        adapter.clear()

        if (addresses.isEmpty()) {
            adapter.showPlaceholder(true)
        } else {
            adapter.showPlaceholder(false)
            adapter.setQuery(query)
            adapter.addItems(addresses)
            recyclerView.scheduleLayoutAnimation()
        }

    }

    private fun onGetAddressRequestFailed() {
        adapter.showPlaceholder(true)
    }

    fun search(query: String?) {
        if (query.isNullOrBlank()) {
            adapter.clear()
            adapter.showPlaceholder(false)
        } else {
            addressGetPresenter.get(query)
        }
    }

    override fun onAddressClicked(address: org.ladlb.directassemblee.model.Address) {
        listener?.onAddressClicked(address)
    }

    interface AddressFragmentListener {

        fun onAddressClicked(address: org.ladlb.directassemblee.model.Address)

    }

}
