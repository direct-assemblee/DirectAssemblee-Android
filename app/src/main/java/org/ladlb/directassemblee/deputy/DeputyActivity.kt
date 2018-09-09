package org.ladlb.directassemblee.deputy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import org.ladlb.directassemblee.AbstractToolBarActivity
import org.ladlb.directassemblee.DashboardActivity
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.timeline.TimelineFragment.DeputyTimeLineFragmentListener

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

class DeputyActivity : AbstractToolBarActivity(), DeputyTimeLineFragmentListener {

    companion object Factory {

        var EXTRA_DEPUTY: String = "EXTRA_DEPUTY"

        fun getIntent(context: Context, deputy: Deputy): Intent {

            val intent = Intent(context, DeputyActivity::class.java)
            intent.putExtra(EXTRA_DEPUTY, deputy)

            return intent

        }

    }

    override fun getContentView(): Int = R.layout.activity_deputy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction().replace(
                R.id.frameLayout,
                DeputyFragment.newInstance(
                        intent.getParcelableExtra(DashboardActivity.EXTRA_DEPUTY)
                ),
                DeputyFragment.TAG
        ).commit()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            setResult(Activity.RESULT_CANCELED)
            super.onOptionsItemSelected(item)
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun onRefreshTimeLine(deputy: Deputy) {
        val fragment = supportFragmentManager.findFragmentByTag(DeputyFragment.TAG)
        if (fragment is DeputyFragment) {
            fragment.setDeputy(deputy)
        }
    }

}