package org.ladlb.directassemblee.address

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_address_search.*
import org.ladlb.directassemblee.AbstractToolBarActivity
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.address.SearchAddressFragment.SearchAddressFragmentListener
import java.util.concurrent.TimeUnit

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

class SearchAddressActivity : AbstractToolBarActivity(), SearchAddressFragmentListener {

    companion object Factory {

        var EXTRA_ADDRESS: String = "EXTRA_ADDRESS"

        fun getIntent(context: Context): Intent = Intent(context, SearchAddressActivity::class.java)

    }

    override fun getContentView(): Int = R.layout.activity_address_search

    private lateinit var searchAddressFragment: SearchAddressFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            searchAddressFragment = SearchAddressFragment.newInstance()
            supportFragmentManager.beginTransaction().replace(
                    R.id.frameLayout,
                    searchAddressFragment,
                    SearchAddressFragment.TAG
            ).commit()
        }

        RxSearchView.queryTextChanges(searchView)
                .debounce {
                    if (isValidQuery(it)) Observable.empty() else Observable.timer(500, TimeUnit.MILLISECONDS)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val query = it.toString()
                    searchAddressFragment.search(if (isValidQuery(query)) null else query)
                }

    }

    private fun isValidQuery(query: CharSequence): Boolean = TextUtils.isEmpty(query) || query.length < 3

    override fun onAddressClicked(address: Address) {
        val returnIntent = Intent()
        returnIntent.putExtra(EXTRA_ADDRESS, address)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

}