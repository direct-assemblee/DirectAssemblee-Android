package org.ladlb.directassemblee.deputy

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DeputyListFragmentProvider {

    @ContributesAndroidInjector
    internal abstract fun provideDeputyListFragment(): DeputyListFragment
}