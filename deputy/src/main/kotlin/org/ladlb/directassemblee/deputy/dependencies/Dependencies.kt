package org.ladlb.directassemblee.deputy.dependencies

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.ladlb.directassemblee.deputy.*

object Dependencies {

    val deputyModule = module {

        single<DeputyRepository> { DeputyRepositoryImpl(get()) }

        single<DeputiesRepository> { DeputiesRepositoryImpl(get()) }

        viewModel {
            DeputyGetPresenter(get())
        }

        viewModel {
            DeputiesGetPresenter(get())
        }

    }

}
