package org.ladlb.directassemblee.address

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.coroutineScope
import kotlinx.android.synthetic.main.activity_address_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.ladlb.directassemblee.address.AddressFragment.AddressFragmentListener
import org.ladlb.directassemblee.core.AbstractToolBarActivity

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

class AddressActivity : AbstractToolBarActivity(), AddressFragmentListener, OnQueryTextListener {

    companion object Factory {

        var EXTRA_ADDRESS: String = "EXTRA_ADDRESS"

        fun getIntent(context: Context): Intent = Intent(context, AddressActivity::class.java)

    }

    override fun getContentView(): Int = R.layout.activity_address_search

    private lateinit var addressFragment: AddressFragment

    private val coroutineScope = lifecycle.coroutineScope

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            addressFragment = AddressFragment.newInstance()
            supportFragmentManager.beginTransaction().replace(
                    R.id.frameLayout,
                    addressFragment,
                    AddressFragment.TAG
            ).commit()
        }

        searchView.setOnQueryTextListener(this)

    }

    private fun isValidQuery(query: CharSequence): Boolean = !TextUtils.isEmpty(query) && query.length >= 3

    override fun onAddressClicked(address: org.ladlb.directassemblee.model.Address) {
        val returnIntent = Intent()
        returnIntent.putExtra(EXTRA_ADDRESS, address)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(query: String?): Boolean {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            query?.let {
                if (isValidQuery(it)) {
                    delay(500)
                }
                addressFragment.search(it)
            }
        }
        return false
    }

}
