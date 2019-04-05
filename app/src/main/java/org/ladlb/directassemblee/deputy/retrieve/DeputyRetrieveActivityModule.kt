package org.ladlb.directassemblee.deputy.retrieve

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.ladlb.directassemblee.location.LocationGetPresenter
import org.ladlb.directassemblee.location.LocationGetPresenter.LocationGetView

@Module
abstract class DeputyRetrieveActivityModule {

    @Binds
    abstract fun provideLocationGetView(deputyRetrieveActivity: DeputyRetrieveActivity): LocationGetView

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideLocationGetPresenter(locationGetView: LocationGetView): LocationGetPresenter {
            return LocationGetPresenter(locationGetView)
        }

    }

}
