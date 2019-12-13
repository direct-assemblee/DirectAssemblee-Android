package org.ladlb.directassemblee.deputy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import org.ladlb.directassemblee.core.AbstractViewModel
import org.ladlb.directassemblee.core.helper.ComparisonHelper
import org.ladlb.directassemblee.model.Deputy
import retrofit2.HttpException

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

class DeputiesGetPresenter(private val repository: DeputiesRepository) : AbstractViewModel() {

    sealed class DeputiesGet {
        class Result(val deputies: List<Deputy>) : DeputiesGet()
        object NotFound : DeputiesGet()
        object Error : DeputiesGet()
    }

    private val _viewState = MutableLiveData<DeputiesGet>()
    val viewState: LiveData<DeputiesGet> = _viewState

    fun getDeputies() {

        launch {
            try {
                onDeputiesReceived(repository.getDeputies())
            } catch (e: Throwable) {
                _viewState.postValue(DeputiesGet.Error)
            }
        }

    }

    fun getDeputies(latitude: Double, longitude: Double) {

        launch {
            try {
                onDeputiesReceived(repository.getDeputies(latitude, longitude))
            } catch (e: Throwable) {
                if (e is HttpException && e.code() == 404) {
                    _viewState.postValue(DeputiesGet.NotFound)
                } else {
                    _viewState.postValue(DeputiesGet.Error)
                }
            }
        }

    }

    private fun onDeputiesReceived(deputies: List<Deputy>) {

        _viewState.postValue(
                DeputiesGet.Result(
                        deputies.sortedWith(
                                ComparisonHelper.compareBy(
                                        Deputy::lastname,
                                        Deputy::firstname
                                )
                        )
                )
        )

    }

}
