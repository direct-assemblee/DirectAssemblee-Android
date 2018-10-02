package org.ladlb.directassemblee.deputy.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_deputy_search.*
import org.ladlb.directassemblee.AbstractToolBarActivity
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter.GetDeputiesView
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.deputy.DeputyActivity
import org.ladlb.directassemblee.deputy.DeputyListFragment
import org.ladlb.directassemblee.deputy.DeputyListFragment.DeputyListFragmentListener
import org.ladlb.directassemblee.helper.ViewHelper

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

open class DeputySearchActivity : AbstractToolBarActivity(), DeputyListFragmentListener, GetDeputiesView {

    companion object Factory {

        fun getIntent(context: Context): Intent = Intent(context, DeputySearchActivity::class.java)

    }

    private lateinit var deputiesGetPresenter: DeputiesGetPresenter

    override fun getContentView(): Int = R.layout.activity_deputy_search

    private var deputyListFragment: DeputyListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        deputiesGetPresenter = DeputiesGetPresenter(
                this,
                lifecycle
        )

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

        deputiesGetPresenter.getDeputies(getApiServices())

    }

    override fun onDeputyClicked(deputy: Deputy) {

        ViewHelper.hideKeyboard(this)

        startActivity(
                DeputyActivity.getIntent(
                        this,
                        deputy
                )
        )

    }

    private fun showDeputyListFragment(deputies: Array<Deputy>) {
        deputyListFragment = DeputyListFragment.newInstance(deputies)
        supportFragmentManager.beginTransaction().replace(
                R.id.frameLayout,
                deputyListFragment!!,
                DeputyListFragment.TAG
        ).commit()
        searchView.visibility = if (deputies.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun onDeputiesReceived(deputies: Array<Deputy>) {
        showDeputyListFragment(deputies)
        loadingView.visibility = View.GONE
    }

    override fun onNoDeputyFound() {
        loadingView.setError(getString(R.string.deputy_not_found))
    }

    override fun onGetDeputiesRequestFailed() {
        loadingView.setError(getString(R.string.get_deputies_request_failed))
    }

}