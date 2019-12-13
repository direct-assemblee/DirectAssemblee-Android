package org.ladlb.directassemblee.synthesis.rate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import org.ladlb.directassemblee.core.AbstractViewModel
import org.ladlb.directassemblee.model.Rate
import org.ladlb.directassemblee.synthesis.rate.RateGetPresenter.RateGet.Error
import org.ladlb.directassemblee.synthesis.rate.RateGetPresenter.RateGet.Result

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

class RateGetPresenter(private val repository: RateRepository) : AbstractViewModel() {

    sealed class RateGet {
        class Result(val rates: List<Rate>) : RateGet()
        object Error : RateGet()
    }

    private val _viewState = MutableLiveData<RateGet>()
    val viewState: LiveData<RateGet> = _viewState

    init {

        launch {
            try {
                repository.getActivityRates().let {
                    _viewState.postValue(Result(it))
                }
            } catch (e: Exception) {
                _viewState.postValue(Error)
            }
        }

    }

}