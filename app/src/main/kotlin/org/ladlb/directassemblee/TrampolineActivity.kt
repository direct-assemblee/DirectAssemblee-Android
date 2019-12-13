package org.ladlb.directassemblee

import android.os.Bundle
import org.koin.android.ext.android.inject
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.UserProperty.Companion.DISTRICT
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.UserProperty.Companion.PARLIAMENT_GROUP
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.core.AbstractActivity
import org.ladlb.directassemblee.deputy.retrieve.DeputyRetrieveActivity
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

class TrampolineActivity : AbstractActivity() {

    private val preferenceStorage: org.ladlb.directassemblee.storage.PreferencesStorage by inject()

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deputy = preferenceStorage.loadDeputy()

        if (deputy == null) {
            firebaseAnalyticsManager.clearUserProperty(PARLIAMENT_GROUP, DISTRICT)
            startRetrieveDeputyActivity()
        } else {
            firebaseAnalyticsManager.setUserProperty(PARLIAMENT_GROUP, deputy.parliamentGroup)
            firebaseAnalyticsManager.setUserProperty(DISTRICT, deputy.getCompleteLocality())
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
