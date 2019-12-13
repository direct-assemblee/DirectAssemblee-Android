package org.ladlb.directassemblee.notification

import android.text.TextUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ladlb.directassemblee.analytics.AnalyticsHelper
import org.ladlb.directassemblee.network.da.ApiServices
import kotlin.coroutines.CoroutineContext

class NotificationRepositoryImpl(private val apiServices: ApiServices, private val preferences: NotificationStorage?, private val analyticsHelper: AnalyticsHelper?, private var coroutineContext: CoroutineContext = Dispatchers.IO) : NotificationRepository {

    override suspend fun postSubscribe(id: String, token: String, deputyId: Int) = withContext(coroutineContext) {

        if (TextUtils.isEmpty(token)) {
            val error = "Token empty in subscribe"
            analyticsHelper?.track(error)
            preferences?.setNotificationEnabled(false)
            throw NullPointerException(error)
        } else {

            try {

                val body = HashMap<String, String>()
                body["instanceId"] = id
                body["token"] = token

                apiServices.postSubscribeAsync(body, deputyId)
                preferences?.setNotificationEnabled(true)

            } catch (e: Throwable) {
                analyticsHelper?.track(e)
                preferences?.setNotificationEnabled(false)
                throw e
            }

        }

        return@withContext

    }

    override suspend fun postUnSubscribe(id: String, token: String, deputyId: Int) = withContext(coroutineContext) {

        if (TextUtils.isEmpty(token)) {
            val error = "Token empty in unsubscribe"
            analyticsHelper?.track(error)
            throw NullPointerException(error)
        } else {

            try {

                val body = HashMap<String, String>()
                body["instanceId"] = id
                body["token"] = token

                apiServices.postUnSubscribeAsync(body, deputyId)
                preferences?.setNotificationEnabled(false)
            } catch (e: Throwable) {
                analyticsHelper?.track(e)
                throw e
            }

        }

        return@withContext

    }

}