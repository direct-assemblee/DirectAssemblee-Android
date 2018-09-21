package org.ladlb.directassemblee.deputy.find

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_deputy_find.*
import org.ladlb.directassemblee.AbstractFragment
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.*
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter.GetDeputiesView
import org.ladlb.directassemblee.deputy.DeputyAdapter.OnDeputyClickListener
import org.ladlb.directassemblee.deputy.DeputyGetPresenter.GetDeputyView
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.ItemKey

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

class DeputyFindFragment : AbstractFragment(), GetDeputiesView, OnDeputyClickListener, GetDeputyView {

    override fun getClassName(): String = "DeputyFindFragment"

    companion object {

        var TAG: String = DeputyFindFragment::class.java.name

        private var ARG_LATITUDE: String = "ARG_LATITUDE"

        private var ARG_LONGITUDE: String = "ARG_LONGITUDE"

        fun newInstance(latitude: Double, longitude: Double): DeputyFindFragment {

            val bundle = Bundle()
            bundle.putDouble(ARG_LATITUDE, latitude)
            bundle.putDouble(ARG_LONGITUDE, longitude)

            val fragment = DeputyFindFragment()
            fragment.arguments = bundle

            return fragment

        }
    }

    private lateinit var adapter: DeputyAdapter

    private lateinit var deputiesGetPresenter: DeputiesGetPresenter

    private lateinit var deputyGetPresenter: DeputyGetPresenter

    private var listener: DeputyFindFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            listener = activity as DeputyFindFragmentListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement DeputyFindFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_deputy_find, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = DeputyAdapter(
                context!!,
                arrayListOf()
        )
        adapter.setOnDeputyClickListener(this)

        deputiesGetPresenter = DeputiesGetPresenter(
                this,
                lifecycle
        )

        deputyGetPresenter = DeputyGetPresenter(
                this,
                lifecycle
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingView.setLabel(getString(R.string.deputy_retrieve_loading_search))

        deputiesGetPresenter.getDeputies(
                getApiServices(),
                arguments!!.getDouble(ARG_LATITUDE),
                arguments!!.getDouble(ARG_LONGITUDE)
        )

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DeputyItemDecorator(resources))

    }

    override fun onDeputiesReceived(deputies: Array<Deputy>) {

        when {
            deputies.isEmpty() -> {
                loadingView.visibility = View.GONE
                showError()
            }
            deputies.size == 1 -> {
                onDeputyClicked(deputies[0])
            }
            else -> {
                loadingView.visibility = View.GONE
                setDeputies(deputies)
            }
        }

    }

    private fun tagDeputyFound(deputy: Deputy) {
        getFireBaseAnalytics().logEvent(
                Event.DEPUTY_FOUND,
                FirebaseAnalyticsHelper.addDeputy(
                        Bundle(),
                        deputy
                )
        )
    }

    private fun setDeputies(deputies: Array<Deputy>) {

        tagMultipleDeputiesFound(deputies)
        linearLayout.visibility = View.VISIBLE
        adapter.clear()
        adapter.addItems(deputies)
        adapter.notifyDataSetChanged()

    }

    private fun tagMultipleDeputiesFound(deputies: Array<Deputy>) {
        val bundle = Bundle()
        bundle.putInt(ItemKey.NUMBER, deputies.size)
        getFireBaseAnalytics().logEvent(
                Event.MULTIPLE_DEPUTIES_FOUND,
                bundle
        )
    }

    private fun showError() {

        AlertDialog.Builder(activity!!).setMessage(
                R.string.deputy_not_found
        ).setPositiveButton(
                R.string.ok,
                { _, _ ->
                    activity!!.onBackPressed()
                }
        ).create().show()

    }

    override fun onDeputyClicked(deputy: Deputy) {

        tagDeputyFound(deputy)

        loadingView.visibility = View.VISIBLE
        loadingView.setLabel(getString(R.string.deputy_retrieve_loading_details))

        deputyGetPresenter.getDeputy(
                getApiServices(),
                deputy.department!!.id,
                deputy.district
        )

    }

    override fun onGetDeputiesRequestFailed() {
        loadingView.setError(getString(R.string.get_deputies_request_failed))
    }

    override fun onDeputyReceived(deputy: Deputy) {
        listener?.onDeputyFind(deputy)
    }

    override fun onNoDeputyFound() {
        loadingView.setError(getString(R.string.deputy_not_found))
    }

    override fun onGetDeputyRequestFailed() {
        loadingView.setError(getString(R.string.get_deputy_request_failed))
    }

    interface DeputyFindFragmentListener {
        fun onDeputyFind(deputy: Deputy)
    }

}
