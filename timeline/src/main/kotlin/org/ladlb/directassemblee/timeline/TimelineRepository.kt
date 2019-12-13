package org.ladlb.directassemblee.timeline

import org.ladlb.directassemblee.model.TimelineItem

interface TimelineRepository {
    suspend fun get(deputyId: Int): List<TimelineItem>
    suspend fun loadMore(deputyId: Int): List<TimelineItem>
    suspend fun clear(deputyId: Int)
}
