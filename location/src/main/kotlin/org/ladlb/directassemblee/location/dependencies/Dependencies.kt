package org.ladlb.directassemblee.location.dependencies

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.ladlb.directassemblee.location.LocationGetPresenter
import org.ladlb.directassemblee.location.LocationRepository

object Dependencies {

    val locationModule = module {

        single {
            LocationRepository()
        }

        viewModel {
            LocationGetPresenter(get())
        }

    }

}