package org.ladlb.directassemblee.core.dependencies

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.ladlb.directassemblee.core.EmptyProvider

class DependenciesProvider : EmptyProvider() {
    override fun onCreate(): Boolean {
        startKoin {
            androidLogger()
            androidContext(context as Application)
        }
        return true
    }
}