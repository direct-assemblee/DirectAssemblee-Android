package org.ladlb.directassemblee.storage.dependencies

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.ladlb.directassemblee.storage.PreferencesStorage
import org.ladlb.directassemblee.storage.PreferencesStorageImpl

object Dependencies {

    val storageModule = module {

        single {
            androidContext().getSharedPreferences(
                    "directAssembee",
                    Context.MODE_PRIVATE
            )
        }

        single<PreferencesStorage> { PreferencesStorageImpl(get()) }

    }

}
