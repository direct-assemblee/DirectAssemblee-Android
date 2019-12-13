package org.ladlb.directassemblee.synthesis.dependencies

import org.koin.core.context.loadKoinModules
import org.ladlb.directassemblee.core.EmptyProvider

class DependenciesProvider : EmptyProvider() {
    override fun onCreate(): Boolean {
        loadKoinModules(
                Dependencies.synthesisModule
        )
        return true
    }
}