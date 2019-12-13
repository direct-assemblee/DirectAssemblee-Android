package org.ladlb.directassemblee.address.dependencies

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.ladlb.directassemblee.address.AddressGetPresenter
import org.ladlb.directassemblee.address.AddressRepository
import org.ladlb.directassemblee.address.AddressRepositoryImpl

object Dependencies {

    val addressModule = module {

        single<AddressRepository> {
            AddressRepositoryImpl(get())
        }

        viewModel {
            AddressGetPresenter(get())
        }

    }

}