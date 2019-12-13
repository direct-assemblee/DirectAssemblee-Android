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
import com.google.android.gms.common.api.ResolvableApiException
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.ladlb.directassemblee.DashboardActivity
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.address.AddressActivity
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.UserProperty.Companion.DISTRICT
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys.UserProperty.Companion.PARLIAMENT_GROUP
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.core.AbstractActivity
import org.ladlb.directassemblee.core.helper.ErrorHelper
import org.ladlb.directassemblee.core.helper.collect
import org.ladlb.directassemblee.deputy.find.DeputyFindActivity
import org.ladlb.directassemblee.deputy.retrieve.DeputyRetrieveFragment.DeputyRetrieveLocationFragmentListener
import org.ladlb.directassemblee.deputy.search.PrimaryDeputySearchActivity
import org.ladlb.directassemblee.helper.isGooglePlayServicesAvailable
import org.ladlb.directassemblee.location.LocationGetPresenter
import org.ladlb.directassemblee.location.LocationRepository.LocationRepositoryResult.*
import org.ladlb.directassemblee.model.Address
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

class DeputyRetrieveActivity : AbstractActivity(), DeputyRetrieveLocationFragmentListener {

    companion object Factory {

        private const val REQUEST_SEARCH_ADDRESS: Int = 1
        private const val REQUEST_ACCESS_FINE_LOCATION: Int = 2
        private const val REQUEST_DEPUTY_SEARCHABLE: Int = 3
        private const val REQUEST_DEPUTY_FIND: Int = 4
        private const val REQUEST_CHECK_SETTINGS: Int = 5

        fun getIntent(context: Context): Intent {
            val intent = Intent(context, DeputyRetrieveActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

    }

    private val locationGetPresenter: LocationGetPresenter by viewModel()

    private val preferenceStorage: org.ladlb.directassemblee.storage.PreferencesStorage by inject()

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                    android.R.id.content,
                    DeputyRetrieveFragment.newInstance(),
                    DeputyRetrieveFragment.TAG
            ).commit()
        }

        locationGetPresenter.viewState.collect(this) {
            when (it) {
                is LocationResult -> onLocationUpdate(it.locationResult)
                is LocationException -> onLocationRequestFailed()
                is LocationResolvableException -> onLocationRequestRequired(it.exception)
            }
        }

    }

    override fun onSearchByNamesClicked() {
        firebaseAnalyticsManager.logEvent(Event.SEARCH_DEPUTY_IN_LIST)
        startDeputySearchableActivity()
    }

    override fun onSearchByGeolocationClicked() {

        if (isGooglePlayServicesAvailable()) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                firebaseAnalyticsManager.logEvent(
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
                AddressActivity.getIntent(this),
                REQUEST_SEARCH_ADDRESS
        )
    }

    private fun onDeputyRetrieved(deputy: Deputy) {

        preferenceStorage.saveDeputy(deputy)

        firebaseAnalyticsManager.setUserProperty(PARLIAMENT_GROUP, deputy.parliamentGroup)
        firebaseAnalyticsManager.setUserProperty(DISTRICT, deputy.getCompleteLocality())

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
                        firebaseAnalyticsManager.logEvent(
                                Event.ADDRESS_SELECTED
                        )
                        val address = intent?.getParcelableExtra<Address>(AddressActivity.EXTRA_ADDRESS)
                        val coordinates = address?.geometry?.coordinates
                        if (coordinates != null && coordinates.size == 2) {
                            retrieveDeputy(
                                    coordinates[1],
                                    coordinates[0]
                            )
                        }

                    }
                    Activity.RESULT_CANCELED -> {
                        firebaseAnalyticsManager.logEvent(
                                Event.BACK_FROM_SELECT_ADDRESS
                        )
                    }
                }

            REQUEST_DEPUTY_FIND ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        if (intent != null) {
                            onDeputyRetrieved(intent.getParcelableExtra(DeputyFindActivity.EXTRA_DEPUTY)!!)
                        }
                    }
                }

            REQUEST_DEPUTY_SEARCHABLE ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        if (intent != null) {
                            onDeputyRetrieved(intent.getParcelableExtra(PrimaryDeputySearchActivity.EXTRA_DEPUTY)!!)
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
                }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }
    }

    private fun onLocationUpdate(location: Location) {
        retrieveDeputy(
                location.latitude,
                location.longitude
        )
    }

    private fun onLocationRequestFailed() {
        ErrorHelper.showErrorAlertDialog(this, R.string.error_geolocation)
    }

    private fun onLocationRequestRequired(exception: ResolvableApiException) {
        exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
    }

}
