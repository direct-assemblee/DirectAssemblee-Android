package org.ladlb.directassemblee.deputy

import androidx.lifecycle.Lifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.ladlb.directassemblee.AbstractPresenter
import org.ladlb.directassemblee.api.ladlb.ApiException
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter.DeputiesGetView
import org.ladlb.directassemblee.helper.ComparisonsHelper
import java.text.Collator
import java.util.*

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

class DeputiesGetPresenter(view: DeputiesGetView?, lifecycle: Lifecycle?) : AbstractPresenter<DeputiesGetView>(view, lifecycle) {

    fun getDeputies(apiRepository: ApiRepository, latitude: Double, longitude: Double) {

        apiRepository.getDeputies(
                latitude, longitude
        ).subscribeOn(
                Schedulers.io()
        ).observeOn(
                AndroidSchedulers.mainThread()
        ).doOnSubscribe {
            disposable -> call(disposable)
        }.subscribe(
                Consumer { deputies -> view?.onDeputiesReceived(deputies) },
                object : AbstractPresenter.AbstractErrorConsumer() {
                    override fun onError(t: Throwable) {
                        if (t is ApiException && t.response.code() == 404) {
                            view?.onNoDeputyFound()
                        } else {
                            view?.onGetDeputiesRequestFailed()
                        }
                    }
                }
        )

    }

    fun getDeputies(apiRepository: ApiRepository) {

        apiRepository.getDeputies(

        ).map { deputies ->
            val coll = Collator.getInstance(Locale.FRANCE)
            coll.strength = Collator.PRIMARY
            deputies.sortedWith(ComparisonsHelper.compareBy(coll, Deputy::firstname, Deputy::lastname)).toTypedArray()
        }.subscribeOn(
                Schedulers.io()
        ).observeOn(
                AndroidSchedulers.mainThread()
        ).doOnSubscribe {
            disposable -> call(disposable)
        }.subscribe(
                Consumer { deputies -> view?.onDeputiesReceived(deputies) },
                object : AbstractPresenter.AbstractErrorConsumer() {
                    override fun onError(t: Throwable) {
                        view?.onGetDeputiesRequestFailed()
                    }
                }
        )

    }

    interface DeputiesGetView : BaseView {

        fun onDeputiesReceived(deputies: Array<Deputy>)

        fun onNoDeputyFound()

        fun onGetDeputiesRequestFailed()

    }

}
