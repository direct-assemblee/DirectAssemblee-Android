package org.ladlb.directassemblee

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import org.ladlb.directassemblee.api.dataGouv.RetrofitAddressRepository
import org.ladlb.directassemblee.api.ladlb.RetrofitApiRepository
import org.ladlb.directassemblee.data.CacheManager
import org.ladlb.directassemblee.firebase.FireBaseAnalyticsManager
import org.ladlb.directassemblee.preferences.PreferencesStorage

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

abstract class AbstractActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun onDialogFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResult(requestCode, resultCode, data)
    }

    fun getApiServices(): RetrofitApiRepository =
            (application as AssembleApplication).getApiServices()

    fun getAddressServices(): RetrofitAddressRepository =
            (application as AssembleApplication).getAddressServices()

    fun getPreferences(): PreferencesStorage = (application as AssembleApplication).getPreferences()

    fun getFireBaseAnalytics(): FireBaseAnalyticsManager =
            (application as AssembleApplication).getFireBaseAnalytics()

    fun getCacheManager(): CacheManager =
            (application as AssembleApplication).getCacheManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFireBaseAnalytics().initInstance(this)

        /*
            Used for make app screenshots
            --------------------------------
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        */

    }

}
