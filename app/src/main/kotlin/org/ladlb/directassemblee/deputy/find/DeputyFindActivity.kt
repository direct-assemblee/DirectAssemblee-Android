package org.ladlb.directassemblee.deputy.find

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import org.ladlb.directassemblee.AbstractToolBarActivity
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.deputy.find.DeputyFindFragment.DeputyFindFragmentListener
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.Event

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

class DeputyFindActivity : AbstractToolBarActivity(), DeputyFindFragmentListener {

    companion object Factory {

        var EXTRA_LATITUDE: String = "EXTRA_LATITUDE"

        var EXTRA_LONGITUDE: String = "EXTRA_LONGITUDE"

        var EXTRA_DEPUTY: String = "EXTRA_DEPUTY"

        fun getIntent(context: Context, latitude: Double, longitude: Double): Intent {

            val intent = Intent(context, DeputyFindActivity::class.java)
            intent.putExtra(EXTRA_LATITUDE, latitude)
            intent.putExtra(EXTRA_LONGITUDE, longitude)
            return intent

        }

    }

    override fun getContentView(): Int = R.layout.activity_deputy_select

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            firebaseAnalyticsManager.logEvent(Event.CANCEL_SEARCH_DEPUTY)
            super.onOptionsItemSelected(item)
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                    R.id.frameLayout,
                    DeputyFindFragment.newInstance(
                            intent.getDoubleExtra(EXTRA_LATITUDE, 0.0),
                            intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0)
                    ),
                    DeputyFindFragment.TAG
            ).commit()
        }

    }

    override fun onDeputyFind(deputy: Deputy) {

        val returnIntent = Intent()
        returnIntent.putExtra(EXTRA_DEPUTY, deputy)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()

    }

}