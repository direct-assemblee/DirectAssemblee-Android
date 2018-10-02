package org.ladlb.directassemblee.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.Lifecycle
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.ladlb.directassemblee.AbstractPresenter
import org.ladlb.directassemblee.helper.MetricHelper
import org.ladlb.directassemblee.location.LocationGetPresenter.GetLocationView

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

class LocationGetPresenter(view: GetLocationView, context: Context, lifecycle: Lifecycle?) : AbstractPresenter<GetLocationView>(view, lifecycle) {

    private var rxLocation: RxLocation = RxLocation(context)

    private var locationRequest: LocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    fun getLocation() {

        rxLocation.settings().checkAndHandleResolution(locationRequest).flatMapObservable(
                { result -> getAddressObservable(result) }
        ).subscribeOn(
                Schedulers.io()
        ).observeOn(
                AndroidSchedulers.mainThread()
        ).doOnSubscribe(
                { disposable -> call(disposable) }
        ).subscribe(
                Consumer { location -> view?.onLocationUpdate(location) },
                object : AbstractPresenter.AbstractErrorConsumer() {
                    override fun onError(t: Throwable) {
                        MetricHelper.track(t)
                        view?.onLocationRequestFailed()
                    }
                }
        )

    }

    @SuppressLint("MissingPermission")
    private fun getAddressObservable(success: Boolean): Observable<Location> {
        return if (success) {
            rxLocation.location().updates(locationRequest)
        } else {
            rxLocation.location().lastLocation().toObservable()
        }
    }

    interface GetLocationView : BaseView {

        fun onLocationUpdate(location: Location)

        fun onLocationRequestFailed()

    }

}
