package org.ladlb.directassemblee.deputy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import org.ladlb.directassemblee.core.AbstractViewModel
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

class DeputyGetPresenter(private val repository: DeputyRepository) : AbstractViewModel() {

    sealed class DeputyGet {
        class Result(val deputy: Deputy) : DeputyGet()
        object Error : DeputyGet()
    }

    private val _viewState = MutableLiveData<DeputyGet>()
    val viewState: LiveData<DeputyGet> = _viewState

    fun getDeputy(departmentId: Int, district: Int) {

        launch {
            try {
                _viewState.postValue(DeputyGet.Result(repository.getDeputy(departmentId, district)))
            } catch (e: Throwable) {
                _viewState.postValue(DeputyGet.Error)
            }
        }

    }

}
