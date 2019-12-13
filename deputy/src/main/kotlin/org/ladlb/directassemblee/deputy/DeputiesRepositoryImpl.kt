package org.ladlb.directassemblee.deputy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.network.da.ApiServices
import kotlin.coroutines.CoroutineContext

class DeputiesRepositoryImpl(private val apiServices: ApiServices, private var coroutineContext: CoroutineContext = Dispatchers.IO) : DeputiesRepository {

    override suspend fun getDeputies(): List<Deputy> = withContext(coroutineContext) {
        apiServices.getAllDeputiesAsync()
    }

    override suspend fun getDeputies(latitude: Double, longitude: Double): List<Deputy> = withContext(coroutineContext) {
        apiServices.getDeputiesAsync(latitude, longitude)
    }

}