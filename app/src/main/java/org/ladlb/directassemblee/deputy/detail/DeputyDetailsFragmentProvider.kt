package org.ladlb.directassemblee.deputy.detail

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DeputyDetailsFragmentProvider {

    @ContributesAndroidInjector
    internal abstract fun provideDeputyDetailsFragment(): DeputyDetailsFragment

}