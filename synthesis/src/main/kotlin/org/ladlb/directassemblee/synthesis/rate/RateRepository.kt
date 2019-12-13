package org.ladlb.directassemblee.synthesis.rate

import org.ladlb.directassemblee.model.Rate

interface RateRepository {

    suspend fun getActivityRates(): List<Rate>

}