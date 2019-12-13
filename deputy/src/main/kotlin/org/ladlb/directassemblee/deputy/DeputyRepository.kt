package org.ladlb.directassemblee.deputy

import org.ladlb.directassemblee.model.Deputy

interface DeputyRepository {
    suspend fun getDeputy(departmentId: Int, district: Int): Deputy
}