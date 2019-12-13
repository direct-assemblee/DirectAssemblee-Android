package org.ladlb.directassemblee.deputy.find

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.fragment_deputy_find.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.ItemKey
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.core.AbstractFragment
import org.ladlb.directassemblee.core.helper.collect
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter
import org.ladlb.directassemblee.deputy.DeputyAdapter.OnDeputyClickListener
import org.ladlb.directassemblee.deputy.DeputyGetPresenter
import org.ladlb.directassemblee.model.Deputy

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

class DeputyFindFragment : AbstractFragment(), OnDeputyClickListener {

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

    private val deputiesGetPresenter: DeputiesGetPresenter by viewModel()

    private val deputyGetPresenter: DeputyGetPresenter by viewModel()

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by inject()

    private lateinit var adapter: org.ladlb.directassemblee.deputy.DeputyAdapter

    private var listener: DeputyFindFragmentListener? = null

    override fun onAttach(context: Context) {
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

        adapter = org.ladlb.directassemblee.deputy.DeputyAdapter(
                arrayListOf()
        )
        adapter.setOnDeputyClickListener(this)

        deputiesGetPresenter.viewState.collect(this) {
            when (it) {
                is DeputiesGetPresenter.DeputiesGet.Result -> onDeputiesReceived(it.deputies)
                is DeputiesGetPresenter.DeputiesGet.NotFound -> onNoDeputyFound()
                is DeputiesGetPresenter.DeputiesGet.Error -> onGetDeputiesRequestFailed()
            }
        }

        deputyGetPresenter.viewState.collect(this) {
            when (it) {
                is DeputyGetPresenter.DeputyGet.Result -> onDeputyReceived(it.deputy)
                is DeputyGetPresenter.DeputyGet.Error -> onGetDeputyRequestFailed()
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingView.setLabel(getString(R.string.deputy_retrieve_loading_search))

        deputiesGetPresenter.getDeputies(
                arguments!!.getDouble(ARG_LATITUDE),
                arguments!!.getDouble(ARG_LONGITUDE)
        )

        recyclerView.adapter = adapter

    }

    private fun onDeputiesReceived(deputies: List<Deputy>) {

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
                linearLayout.visibility = View.VISIBLE
            }
        }

    }

    private fun tagDeputyFound(deputy: Deputy) {
        firebaseAnalyticsManager.logEvent(
                Event.DEPUTY_FOUND,
                FirebaseAnalyticsHelper.addDeputy(
                        Bundle(),
                        deputy
                )
        )
    }

    private fun setDeputies(deputies: List<Deputy>) {

        tagMultipleDeputiesFound(deputies)
        linearLayout.visibility = View.VISIBLE
        adapter.clear()
        adapter.addItems(deputies)
        adapter.notifyDataSetChanged()

    }

    private fun tagMultipleDeputiesFound(deputies: List<Deputy>) {
        val bundle = Bundle()
        bundle.putInt(ItemKey.NUMBER, deputies.size)
        firebaseAnalyticsManager.logEvent(
                Event.MULTIPLE_DEPUTIES_FOUND,
                bundle
        )
    }

    private fun showError() {

        AlertDialog.Builder(activity!!).setMessage(
                R.string.deputy_not_found
        ).setPositiveButton(
                R.string.ok
        ) { _, _ ->
            activity!!.onBackPressed()
        }.create().show()

    }

    override fun onDeputyClicked(deputy: Deputy) {

        tagDeputyFound(deputy)

        loadingView.visibility = View.VISIBLE
        loadingView.setLabel(getString(R.string.deputy_retrieve_loading_details))

        deputyGetPresenter.getDeputy(
                deputy.department!!.id,
                deputy.district
        )

    }

    private fun onGetDeputiesRequestFailed() {
        loadingView.setError(getString(R.string.get_deputies_request_failed))
    }

    private fun onDeputyReceived(deputy: Deputy) {
        listener?.onDeputyFind(deputy)
    }

    private fun onNoDeputyFound() {
        loadingView.setError(getString(R.string.deputy_not_found))
    }

    private fun onGetDeputyRequestFailed() {
        loadingView.setError(getString(R.string.get_deputy_request_failed))
    }

    interface DeputyFindFragmentListener {
        fun onDeputyFind(deputy: Deputy)
    }

}
