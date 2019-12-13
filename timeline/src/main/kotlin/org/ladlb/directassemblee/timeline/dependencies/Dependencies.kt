package org.ladlb.directassemblee.timeline.dependencies

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.ladlb.directassemblee.timeline.TimelineCacheManager
import org.ladlb.directassemblee.timeline.TimelineGetPresenter
import org.ladlb.directassemblee.timeline.TimelineRepository
import org.ladlb.directassemblee.timeline.TimelineRepositoryImpl

object Dependencies {

    val timelineModule = module {

        single { TimelineCacheManager() }

        single<TimelineRepository> { TimelineRepositoryImpl(get(), get()) }

        viewModel { (deputyId: Int) ->
            TimelineGetPresenter(get(), deputyId)
        }

    }

}
