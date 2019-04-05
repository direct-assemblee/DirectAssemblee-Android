package org.ladlb.directassemblee.deputy.retrieve

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DeputyRetrieveFragmentProvider {

    @ContributesAndroidInjector
    internal abstract fun provideDeputyRetrieveFragment(): DeputyRetrieveFragment
}
