package org.ladlb.directassemblee.address

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SearchFragmentProvider {

    @ContributesAndroidInjector(modules = [SearchAddressFragmentModule::class])
    internal abstract fun provideSearchAddressFragment(): SearchAddressFragment
}
