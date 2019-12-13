package org.ladlb.directassemblee.location

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Looper
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.ladlb.directassemblee.location.LocationRepository.LocationRepositoryResult.LocationException
import org.ladlb.directassemblee.location.LocationRepository.LocationRepositoryResult.LocationResolvableException

@Suppress("EXPERIMENTAL_API_USAGE")
class LocationRepository {

    sealed class LocationRepositoryResult {
        class LocationResult(val locationResult: Location) : LocationRepositoryResult()
        class LocationResolvableException(val exception: ResolvableApiException) : LocationRepositoryResult()
        class LocationException(val exception: Exception) : LocationRepositoryResult()
    }

    private val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

    private var channel = ConflatedBroadcastChannel<LocationRepositoryResult>()

    @SuppressLint("MissingPermission")
    fun getLocation(activity: Activity): Flow<LocationRepositoryResult> {

        val settingsClient = LocationServices.getSettingsClient(activity)
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        val task = settingsClient.checkLocationSettings(builder.build())

        task.addOnSuccessListener {

            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            super.onLocationResult(result)

                            fusedLocationClient.removeLocationUpdates(this)

                            channel.offer(
                                    LocationRepositoryResult.LocationResult(
                                            result.lastLocation
                                    )
                            )

                        }
                    },
                    Looper.myLooper()
            )

        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                channel.offer(LocationResolvableException(exception))
            } else {
                channel.offer(LocationException(exception))
            }
        }

        return channel.asFlow()

    }

}
