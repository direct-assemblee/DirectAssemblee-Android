package org.ladlb.directassemblee.deputy

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.ladlb.directassemblee.deputy.DeputyGetPresenter.DeputyGetView

@Module
abstract class DeputyFragmentModule {

    @Binds
    internal abstract fun provideDeputyGetView(deputyFragment: DeputyFragment): DeputyGetView

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideDeputyGetPresenter(view: DeputyGetView): DeputyGetPresenter {
            return DeputyGetPresenter(view)
        }

    }


}