package org.ladlb.directassemblee.timeline

import android.util.SparseArray
import androidx.collection.LruCache
import androidx.core.util.forEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimelineCacheManager @Inject constructor() {

    companion object {

        private const val cacheSize = 10 * 1024 * 1024

    }

    private val cache = LruCache<Int, SparseArray<Array<TimelineItem>>>(cacheSize)

    fun put(deputyId: Int, pageId: Int, items: Array<TimelineItem>) {

        var timelineItems = cache.get(deputyId)
        if (timelineItems == null) {
            timelineItems = SparseArray()
        }

        timelineItems.put(
                pageId,
                items
        )

        cache.put(deputyId, timelineItems)

    }

    fun get(deputyId: Int, page: Int) = cache.get(deputyId)?.get(page)

    fun getAll(deputyId: Int): Pair<Int, Array<TimelineItem>>? {

        val timelineItems = cache.get(deputyId)
        return if (timelineItems == null) {
            null
        } else {
            Pair(
                    timelineItems.size(),
                    arrayListOf<TimelineItem>().apply {
                        timelineItems.forEach { _, item -> addAll(item) }
                    }.toTypedArray()
            )
        }
    }

}
