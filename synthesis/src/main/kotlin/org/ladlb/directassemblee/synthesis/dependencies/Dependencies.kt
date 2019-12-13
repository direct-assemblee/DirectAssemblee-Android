package org.ladlb.directassemblee.synthesis.dependencies

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.ladlb.directassemblee.synthesis.rate.RateGetPresenter
import org.ladlb.directassemblee.synthesis.rate.RateRepository
import org.ladlb.directassemblee.synthesis.rate.RateRepositoryImpl


object Dependencies {

    val synthesisModule = module {

        single<RateRepository> { RateRepositoryImpl(get()) }

        viewModel { RateGetPresenter(get()) }

    }

}