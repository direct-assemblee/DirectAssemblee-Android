package org.ladlb.directassemblee.deputy.search

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter.DeputiesGetView

@Module
abstract class DeputySearchActivityModule {

    @Binds
    internal abstract fun provideDeputiesGetView(deputySearchActivity: DeputySearchActivity): DeputiesGetView

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideDeputiesGetPresenter(view: DeputiesGetView): DeputiesGetPresenter {
            return DeputiesGetPresenter(view)
        }

    }

}
