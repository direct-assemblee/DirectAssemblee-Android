package org.ladlb.directassemblee.timeline

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.ladlb.directassemblee.timeline.TimelineGetPresenter.TimelineGetView

@Module
abstract class TimeLineFragmentModule {

    @Binds
    internal abstract fun provideTimelineGetView(timelineFragment: TimelineFragment): TimelineGetView

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideTimelineGetPresenter(view: TimelineGetView): TimelineGetPresenter {
            return TimelineGetPresenter(view)
        }

    }

}