package org.ladlb.directassemblee.deputy

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DeputyFragmentProvider {

    @ContributesAndroidInjector(modules = [DeputyFragmentModule::class])
    internal abstract fun provideDeputyFragment(): DeputyFragment

}