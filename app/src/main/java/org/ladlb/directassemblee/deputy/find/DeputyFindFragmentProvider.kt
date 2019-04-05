package org.ladlb.directassemblee.deputy.find

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DeputyFindFragmentProvider {

    @ContributesAndroidInjector(modules = [DeputyFindFragmentModule::class])
    internal abstract fun provideDeputyFindFragment(): DeputyFindFragment
}
