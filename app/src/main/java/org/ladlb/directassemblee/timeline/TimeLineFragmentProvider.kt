package org.ladlb.directassemblee.timeline

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TimeLineFragmentProvider {

    @ContributesAndroidInjector(modules = [TimeLineFragmentModule::class])
    internal abstract fun provideTimeLineFragment(): TimelineFragment

}