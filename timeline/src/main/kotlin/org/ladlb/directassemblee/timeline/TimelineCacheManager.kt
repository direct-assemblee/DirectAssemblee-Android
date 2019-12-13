package org.ladlb.directassemblee.timeline

import android.util.SparseArray
import androidx.collection.LruCache
import org.ladlb.directassemblee.model.TimelineItem

class TimelineCacheManager {

    companion object {
        private const val cacheSize = 10 * 1024 * 1024
    }

    private val cache = LruCache<Int, SparseArray<List<TimelineItem>>>(cacheSize)

    fun put(deputyId: Int, pageId: Int, items: List<TimelineItem>) {
        val timelineItems = cache.get(deputyId) ?: SparseArray()
        timelineItems.put(pageId, items)
        cache.put(deputyId, timelineItems)
    }

    fun get(deputyId: Int, page: Int) = cache.get(deputyId)?.get(page)

    fun getAll(deputyId: Int): Pair<Int, SparseArray<List<TimelineItem>>>? = cache.get(deputyId)?.let {
        Pair(deputyId, it)
    }

    fun size(deputyId: Int): Int = cache.get(deputyId)?.size() ?: 0

    fun clear(deputyId: Int) {
        cache.remove(deputyId)
    }

}
