package org.ladlb.directassemblee.rate

import androidx.lifecycle.Lifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.ladlb.directassemblee.AbstractPresenter
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.rate.RateGetPresenter.RateGetView

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

class RateGetPresenter(view: RateGetView, lifecycle: Lifecycle?) : AbstractPresenter<RateGetView>(view, lifecycle) {

    fun getActivityRates(apiRepository: ApiRepository) {

        apiRepository.getActivityRates(

        ).subscribeOn(
                Schedulers.io()
        ).observeOn(
                AndroidSchedulers.mainThread()
        ).doOnSubscribe(
                { disposable -> call(disposable) }
        ).subscribe(
                Consumer { rates -> view?.onActivityRatesReceived(rates) },
                object : AbstractPresenter.AbstractErrorConsumer() {
                    override fun onError(t: Throwable) {
                        view?.onGetActivityRatesRequestFailed()
                    }
                }
        )

    }

    interface RateGetView : BaseView {

        fun onActivityRatesReceived(rates: Array<Rate>)

        fun onGetActivityRatesRequestFailed()

    }

}