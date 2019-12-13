package org.ladlb.directassemblee.deputy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.network.da.ApiServices
import kotlin.coroutines.CoroutineContext

class DeputyRepositoryImpl(private val apiServices: ApiServices, private var coroutineContext: CoroutineContext = Dispatchers.IO) : DeputyRepository {

    override suspend fun getDeputy(departmentId: Int, district: Int): Deputy = withContext(coroutineContext) {
        apiServices.getDeputyAsync(departmentId, district)
    }

}