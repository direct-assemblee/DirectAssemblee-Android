package org.ladlb.directassemblee.notification.dependencies

import org.koin.core.context.loadKoinModules
import org.ladlb.directassemblee.core.EmptyProvider

class DependenciesProvider : EmptyProvider() {
    override fun onCreate(): Boolean {
        loadKoinModules(
                Dependencies.notificationModule
        )
        return true
    }
}