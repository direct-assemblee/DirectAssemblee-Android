package org.ladlb.directassemblee.deputy.find

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter
import org.ladlb.directassemblee.deputy.DeputiesGetPresenter.DeputiesGetView
import org.ladlb.directassemblee.deputy.DeputyGetPresenter
import org.ladlb.directassemblee.deputy.DeputyGetPresenter.DeputyGetView

@Module
abstract class DeputyFindFragmentModule {

    @Binds
    internal abstract fun provideDeputyGetView(deputyFindFragment: DeputyFindFragment): DeputyGetView

    @Binds
    internal abstract fun provideDeputiesGetView(deputyFindFragment: DeputyFindFragment): DeputiesGetView

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideDeputyGetPresenter(view: DeputyGetView): DeputyGetPresenter {
            return DeputyGetPresenter(view)
        }

        @Provides
        @JvmStatic
        internal fun provideDeputiesGetPresenter(view: DeputiesGetView): DeputiesGetPresenter {
            return DeputiesGetPresenter(view)
        }

    }

}