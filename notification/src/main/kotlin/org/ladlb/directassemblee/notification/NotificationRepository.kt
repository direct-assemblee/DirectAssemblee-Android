package org.ladlb.directassemblee.notification

interface NotificationRepository {
    suspend fun postSubscribe(id: String, token: String, deputyId: Int)
    suspend fun postUnSubscribe(id: String, token: String, deputyId: Int)
}