package org.ladlb.directassemblee.timeline

import androidx.core.util.forEach
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ladlb.directassemblee.model.TimelineItem
import org.ladlb.directassemblee.network.da.ApiServices
import kotlin.coroutines.CoroutineContext

class TimelineRepositoryImpl(private val apiServices: ApiServices, private val timelineCacheManager: TimelineCacheManager, private var coroutineContext: CoroutineContext = Dispatchers.IO) : TimelineRepository {

    override suspend fun get(deputyId: Int): List<TimelineItem> = withContext(coroutineContext) {
        timelineCacheManager.getAll(deputyId)?.second?.let {
            mutableListOf<TimelineItem>().apply {
                it.forEach { _, item -> addAll(item) }
            }.toList()
        } ?: loadMore(deputyId)
    }

    override suspend fun loadMore(deputyId: Int): List<TimelineItem> = withContext(coroutineContext) {
        val page = timelineCacheManager.size(deputyId)
        val result = apiServices.getTimelineAsync(deputyId, page)
        timelineCacheManager.put(deputyId, page, result)
        result
    }

    override suspend fun clear(deputyId: Int) = withContext(coroutineContext) {
        timelineCacheManager.clear(deputyId)
    }
}
