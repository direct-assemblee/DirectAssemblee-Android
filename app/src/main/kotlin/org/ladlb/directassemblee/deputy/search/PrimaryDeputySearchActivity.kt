package org.ladlb.directassemblee.deputy.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.Deputy
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

class PrimaryDeputySearchActivity : DeputySearchActivity() {

    companion object Factory {

        var EXTRA_DEPUTY: String = "EXTRA_DEPUTY"

        fun getIntent(context: Context): Intent = Intent(context, PrimaryDeputySearchActivity::class.java)

    }

    override fun onDeputyClicked(deputy: Deputy) {

        ViewHelper.hideKeyboard(this)

        AlertDialog.Builder(this).setMessage(
                getString(
                        R.string.confirm_follow_deputy,
                        deputy.getCompleteName()
                )
        ).setPositiveButton(
                R.string.ok
        ) { _, _ ->
            onDeputyConfirmed(deputy)
        }.setNegativeButton(
                R.string.cancel
        ) { _, _ ->
        }.create().show()

    }

    private fun setResultAndFinish(deputy: Deputy) {
        val returnIntent = Intent()
        returnIntent.putExtra(EXTRA_DEPUTY, deputy)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun onDeputyConfirmed(deputy: Deputy) {
        setResultAndFinish(deputy)
    }

}