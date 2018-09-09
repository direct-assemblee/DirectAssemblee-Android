package org.ladlb.directassemblee

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.ladlb.directassemblee.AbstractPresenter.BaseView
import org.ladlb.directassemblee.helper.MetricHelper

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

abstract class AbstractPresenter<K : BaseView>(view: K, lifecycle: Lifecycle?) : LifecycleObserver {

    interface BaseView

    var view: K? = view
        private set

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        lifecycle?.addObserver(this)
    }

    protected fun call(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun dispose() {
        compositeDisposable.dispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        view = null
        dispose()
    }

    abstract class AbstractErrorConsumer : Consumer<Throwable> {
        override fun accept(t: Throwable) {
            MetricHelper.track(t)
            onError(t)
        }

        abstract fun onError(t: Throwable)

    }

}
