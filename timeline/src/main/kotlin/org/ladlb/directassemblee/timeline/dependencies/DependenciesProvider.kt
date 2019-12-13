package org.ladlb.directassemblee.timeline.dependencies

import org.koin.core.context.loadKoinModules
import org.ladlb.directassemblee.core.EmptyProvider
import org.ladlb.directassemblee.timeline.dependencies.Dependencies.timelineModule

class DependenciesProvider : EmptyProvider() {
    override fun onCreate(): Boolean {
        loadKoinModules(
                timelineModule
        )
        return true
    }
}