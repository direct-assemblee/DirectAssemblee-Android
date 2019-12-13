package org.ladlb.directassemblee.ballot.dependencies

import org.koin.core.context.loadKoinModules
import org.ladlb.directassemblee.ballot.dependencies.Dependencies.ballotModule
import org.ladlb.directassemblee.core.EmptyProvider

class DependenciesProvider : EmptyProvider() {
    override fun onCreate(): Boolean {
        loadKoinModules(
                ballotModule
        )
        return true
    }
}