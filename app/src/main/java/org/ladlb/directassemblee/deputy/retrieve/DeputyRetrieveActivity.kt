package org.ladlb.directassemblee.deputy.retrieve

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.ladlb.directassemblee.AbstractActivity
import org.ladlb.directassemblee.DashboardActivity
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.address.Address
import org.ladlb.directassemblee.address.SearchAddressActivity
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.deputy.find.DeputyFindActivity
import org.ladlb.directassemblee.deputy.retrieve.DeputyRetrieveFragment.DeputyRetrieveLocationFragmentListener
import org.ladlb.directassemblee.deputy.search.PrimaryDeputySearchActivity
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.helper.ErrorHelper
import org.ladlb.directassemblee.helper.GoogleHelper
import org.ladlb.directassemblee.location.LocationGetPresenter
import org.ladlb.directassemblee.location.LocationGetPresenter.LocationGetView
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

class DeputyRetrieveActivity : AbstractActivity(), DeputyRetrieveLocationFragmentListener, LocationGetView {

    companion object Factory {

        private const val REQUEST_SEARCH_ADDRESS: Int = 1
        private const val REQUEST_ACCESS_FINE_LOCATION: Int = 2
        private const val REQUEST_DEPUTY_SEARCHABLE: Int = 3
        private const val REQUEST_DEPUTY_FIND: Int = 4

        fun getIntent(context: Context): Intent {
            val intent = Intent(context, DeputyRetrieveActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

    }

    @Inject
    lateinit var locationGetPresenter: LocationGetPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                    android.R.id.content,
                    DeputyRetrieveFragment.newInstance(),
                    DeputyRetrieveFragment.TAG
            ).commit()
        }

    }

    override fun onSearchByNamesClicked() {
        getFireBaseAnalytics().logEvent(Event.SEARCH_DEPUTY_IN_LIST)
        startDeputySearchableActivity()
    }

    override fun onSearchByGeolocationClicked() {

        if (GoogleHelper.isGooglePlayServicesAvailable(this)) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getFireBaseAnalytics().logEvent(
                        Event.SEARCH_DEPUTY_GEOLOCATION
                )
                locationGetPresenter.getLocation(this)
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_ACCESS_FINE_LOCATION
                )
            }

        }

    }

    override fun onSearchByAddressClicked() {
        startPlaceAutocompleteActivity()
    }

    private fun startDeputySearchableActivity() {
        startActivityForResult(
                PrimaryDeputySearchActivity.getIntent(this),
                REQUEST_DEPUTY_SEARCHABLE
        )
    }

    private fun startPlaceAutocompleteActivity() {
        startActivityForResult(
                SearchAddressActivity.getIntent(this),
                REQUEST_SEARCH_ADDRESS
        )
    }

    private fun onDeputyRetrieved(deputy: Deputy) {

        val preferences = getPreferences()
        preferences.saveDeputy(deputy)

        getFireBaseAnalytics().setUserDeputyProperties(deputy)

        startDeputyActivity(deputy)
        finish()

    }

    private fun startDeputyActivity(deputy: Deputy) {

        startActivity(
                DashboardActivity.getIntent(
                        this,
                        deputy
                )
        )

    }

    private fun retrieveDeputy(latitude: Double, longitude: Double) {
        startActivityForResult(
                DeputyFindActivity.getIntent(
                        this,
                        latitude,
                        longitude
                ),
                REQUEST_DEPUTY_FIND
        )
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        when (requestCode) {

            REQUEST_SEARCH_ADDRESS ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        getFireBaseAnalytics().logEvent(
                                Event.ADDRESS_SELECTED
                        )
                        val address = intent?.getParcelableExtra<Address>(SearchAddressActivity.EXTRA_ADDRESS)
                        val coordinates = address?.geometry?.coordinates
                        if (coordinates != null && coordinates.size == 2) {
                            retrieveDeputy(
                                    coordinates[1],
                                    coordinates[0]
                            )
                        }

                    }
                    Activity.RESULT_CANCELED -> {
                        getFireBaseAnalytics().logEvent(
                                Event.BACK_FROM_SELECT_ADDRESS
                        )
                    }
                }

            REQUEST_DEPUTY_FIND ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        if (intent != null) {
                            onDeputyRetrieved(intent.getParcelableExtra(DeputyFindActivity.EXTRA_DEPUTY))
                        }
                    }
                }

            REQUEST_DEPUTY_SEARCHABLE ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        if (intent != null) {
                            onDeputyRetrieved(intent.getParcelableExtra(PrimaryDeputySearchActivity.EXTRA_DEPUTY))
                        }
                    }
                }

            else -> super.onActivityResult(requestCode, resultCode, intent)

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {

        when (requestCode) {

            REQUEST_ACCESS_FINE_LOCATION ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onSearchByGeolocationClicked()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }
    }

    override fun onLocationUpdate(location: Location) {
        retrieveDeputy(
                location.latitude,
                location.longitude
        )
    }

    override fun onLocationRequestFailed() {
        ErrorHelper.showErrorAlertDialog(this, R.string.error_geolocation)
    }

}
