package org.ladlb.directassemblee.deputy

import kotlinx.coroutines.launch
import org.ladlb.directassemblee.AbstractPresenter
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter.DeputiesGetView
import org.ladlb.directassemblee.helper.ComparisonHelper
import retrofit2.HttpException
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

class DeputiesGetPresenter @Inject
constructor(val apiRepository: ApiRepository, view: DeputiesGetView) : AbstractPresenter<DeputiesGetView>(view) {

    fun getDeputies() {

        launch {
            try {
                onDeputiesReceived(apiRepository.getDeputies())
            } catch (e: Throwable) {
                view?.onGetDeputiesRequestFailed()
            }
        }

    }

    fun getDeputies(latitude: Double, longitude: Double) {

        launch {
            try {
                onDeputiesReceived(apiRepository.getDeputies(latitude, longitude))
            } catch (e: Throwable) {
                if (e is HttpException && e.code() == 404) {
                    view?.onNoDeputyFound()
                } else {
                    view?.onGetDeputiesRequestFailed()
                }
            }
        }

    }

    private fun onDeputiesReceived(deputies: Array<Deputy>) {

        view?.onDeputiesReceived(
                deputies.sortedWith(
                        ComparisonHelper.compareBy(
                                Deputy::lastname,
                                Deputy::firstname
                        )
                ).toTypedArray()
        )

    }

    interface DeputiesGetView : BaseView {

        fun onDeputiesReceived(deputies: Array<Deputy>)

        fun onNoDeputyFound()

        fun onGetDeputiesRequestFailed()

    }

}
