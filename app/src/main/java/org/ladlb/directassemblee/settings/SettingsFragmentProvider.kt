package org.ladlb.directassemblee.settings

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsFragmentProvider {

    @ContributesAndroidInjector(modules = [SettingsFragmentModule::class])
    internal abstract fun provideSettingsFragment(): SettingsFragment

}