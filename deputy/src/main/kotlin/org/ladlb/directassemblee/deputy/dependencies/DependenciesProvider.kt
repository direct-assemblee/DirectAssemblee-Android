package org.ladlb.directassemblee.deputy.dependencies

import org.koin.core.context.loadKoinModules
import org.ladlb.directassemblee.core.EmptyProvider
import org.ladlb.directassemblee.deputy.dependencies.Dependencies.deputyModule

class DependenciesProvider : EmptyProvider() {
    override fun onCreate(): Boolean {
        loadKoinModules(
                deputyModule
        )
        return true
    }
}