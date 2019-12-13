package org.ladlb.directassemblee.deputy.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_deputy_search.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.core.AbstractToolBarActivity
import org.ladlb.directassemblee.core.helper.ViewHelper
import org.ladlb.directassemblee.core.helper.collect
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter.DeputiesGet.*
import org.ladlb.directassemblee.deputy.DeputyListFragment
import org.ladlb.directassemblee.deputy.DeputyListFragment.DeputyListFragmentListener
import org.ladlb.directassemblee.deputy.DeputyTimelineActivity
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

open class DeputySearchActivity : AbstractToolBarActivity(), DeputyListFragmentListener {

    companion object Factory {

        fun getIntent(context: Context): Intent = Intent(context, DeputySearchActivity::class.java)

    }

    private val deputiesGetPresenter: DeputiesGetPresenter by viewModel()

    override fun getContentView(): Int = R.layout.activity_deputy_search

    private var deputyListFragment: DeputyListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchView.visibility = View.GONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                applyQuery(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                applyQuery(query)
                return false
            }

            fun applyQuery(query: String) {
                deputyListFragment?.filter?.filter(query)
            }
        })

        deputiesGetPresenter.viewState.collect(this) {
            when (it) {
                is Result -> onDeputiesReceived(it.deputies)
                is NotFound -> onNoDeputyFound()
                is Error -> onGetDeputiesRequestFailed()
            }
        }

        deputiesGetPresenter.getDeputies()

    }

    override fun onDeputyClicked(deputy: Deputy) {

        ViewHelper.hideKeyboard(this)

        startActivity(
                DeputyTimelineActivity.getIntent(
                        this,
                        deputy
                )
        )

    }

    private fun showDeputyListFragment(deputies: List<Deputy>) {
        deputyListFragment = DeputyListFragment.newInstance(deputies)
        supportFragmentManager.beginTransaction().replace(
                R.id.frameLayout,
                deputyListFragment!!,
                DeputyListFragment.TAG
        ).commit()
        searchView.visibility = if (deputies.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun onDeputiesReceived(deputies: List<Deputy>) {
        showDeputyListFragment(deputies)
        loadingView.visibility = View.GONE
    }

    private fun onNoDeputyFound() {
        loadingView.setError(getString(R.string.deputy_not_found))
    }

    private fun onGetDeputiesRequestFailed() {
        loadingView.setError(getString(R.string.get_deputies_request_failed))
    }

}