package org.ladlb.directassemblee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.deputy.retrieve.DeputyRetrieveActivity
import org.ladlb.directassemblee.preferences.PreferencesStorageImpl
import javax.inject.Inject

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

class TrampolineActivity : AbstractActivity() {

    @Inject
    lateinit var preferenceStorage: PreferencesStorageImpl

    companion object {

        fun getIntent(context: Context): Intent = Intent(
                context,
                TrampolineActivity::class.java
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deputy = preferenceStorage.loadDeputy()

        if (deputy == null) {
            firebaseAnalyticsManager.clearUserDeputyProperties()
            startRetrieveDeputyActivity()
        } else {
            firebaseAnalyticsManager.setUserDeputyProperties(deputy)
            startDeputyActivity(deputy)
        }
        finish()

    }

    private fun startRetrieveDeputyActivity() {
        startActivity(DeputyRetrieveActivity.getIntent(this))
    }

    private fun startDeputyActivity(deputy: Deputy) {
        startActivity(DashboardActivity.getIntent(this, deputy))
    }

}
