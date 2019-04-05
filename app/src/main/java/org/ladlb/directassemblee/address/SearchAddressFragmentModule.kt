package org.ladlb.directassemblee.address

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.ladlb.directassemblee.address.AddressGetPresenter.AddressGetView

@Module
abstract class SearchAddressFragmentModule {

    @Binds
    internal abstract fun provideAddressGetView(searchAddressFragment: SearchAddressFragment): AddressGetView

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideAddressGetPresenter(view: AddressGetView): AddressGetPresenter {
            return AddressGetPresenter(view)
        }

    }

}