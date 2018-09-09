package org.ladlb.directassemblee.deputy.retrieve

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_deputy_retrieve_location.*
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

class DeputyRetrieveFragment : AbstractFragment(), OnClickListener {

    override fun getClassName(): String = "DeputyRetrieveFragment"

    companion object {

        var TAG: String = DeputyRetrieveFragment::class.java.name

        fun newInstance(): DeputyRetrieveFragment = DeputyRetrieveFragment()

    }

    private var mListener: DeputyRetrieveLocationFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mListener = activity as DeputyRetrieveLocationFragmentListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement DeputyRetrieveLocationFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_deputy_retrieve_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewName.setOnClickListener(this)
        textViewGeolocation.setOnClickListener(this)
        textViewAddress.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.textViewName -> mListener?.onSearchByNamesClicked()
            R.id.textViewGeolocation -> mListener?.onSearchByGeolocationClicked()
            R.id.textViewAddress -> mListener?.onSearchByAddressClicked()
        }

    }

    interface DeputyRetrieveLocationFragmentListener {
        fun onSearchByNamesClicked()

        fun onSearchByGeolocationClicked()

        fun onSearchByAddressClicked()
    }

}
