package org.ladlb.directassemblee.synthesis.rate

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ladlb.directassemblee.model.Rate
import org.ladlb.directassemblee.network.da.ApiServices
import kotlin.coroutines.CoroutineContext

class RateRepositoryImpl(private val apiServices: ApiServices, private var coroutineContext: CoroutineContext = Dispatchers.IO) : RateRepository {

    override suspend fun getActivityRates(): List<Rate> = withContext(coroutineContext) {
        apiServices.getActivityRatesAsync()
    }

}