package org.ladlb.directassemblee.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_settings.*
import org.ladlb.directassemblee.AbstractToolBarActivity
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.settings.SettingsFragment.SettingsFragmentListener

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

class SettingsActivity : AbstractToolBarActivity(), SettingsFragmentListener {

    companion object Factory {

        fun getIntent(context: Context): Intent = Intent(
                context,
                SettingsActivity::class.java
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                    R.id.frameLayout,
                    SettingsFragment.newInstance(),
                    SettingsFragment.TAG
            ).commit()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.settings)
    }

    override fun showLoadingView(label: CharSequence) {
        loadingView.setLabel(label)
        loadingView.visibility = View.VISIBLE
    }

    override fun hideLoadingView() {
        loadingView.visibility = View.GONE
    }

    override fun getContentView(): Int = R.layout.activity_settings

}
