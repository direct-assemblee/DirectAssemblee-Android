package org.ladlb.directassemblee.deputy

import org.ladlb.directassemblee.model.Deputy

interface DeputiesRepository {
    suspend fun getDeputies(): List<Deputy>
    suspend fun getDeputies(latitude: Double, longitude: Double): List<Deputy>
}