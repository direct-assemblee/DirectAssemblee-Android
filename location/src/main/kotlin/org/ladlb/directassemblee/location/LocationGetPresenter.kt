package org.ladlb.directassemblee.location

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.ladlb.directassemblee.core.AbstractViewModel

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

class LocationGetPresenter(private val locationRepository: LocationRepository) : AbstractViewModel() {

    private val _viewState = MutableLiveData<LocationRepository.LocationRepositoryResult>()
    val viewState: LiveData<LocationRepository.LocationRepositoryResult> = _viewState

    fun getLocation(activity: Activity) {

        launch {
            locationRepository.getLocation(activity).collect {
                _viewState.postValue(it)
            }
        }

    }

}
