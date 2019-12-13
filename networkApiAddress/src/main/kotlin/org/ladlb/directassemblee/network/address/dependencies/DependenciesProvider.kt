package org.ladlb.directassemblee.network.address.dependencies

import org.koin.core.context.loadKoinModules
import org.ladlb.directassemblee.core.EmptyProvider

class DependenciesProvider : EmptyProvider() {
    override fun onCreate(): Boolean {
        loadKoinModules(
                Dependencies.networkApiAdressModule
        )
        return true
    }
}
