package org.ladlb.directassemblee.notification

import android.content.SharedPreferences

class NotificationStorage(private val preferences: SharedPreferences) {

    companion object {

        private const val KEY_NOTIFICATION = "KEY_NOTIFICATION"
        private const val KEY_FIREBASE_TOKEN = "KEY_FIREBASE_TOKEN"

    }

    fun setNotificationEnabled(isEnable: Boolean) {
        preferences.edit().putBoolean(
                KEY_NOTIFICATION,
                isEnable
        ).apply()
    }

    fun isNotificationEnabled(): Boolean {
        return preferences.getBoolean(
                KEY_NOTIFICATION,
                false
        )
    }

    fun getFirebaseToken(): String? {
        return preferences.getString(
                KEY_FIREBASE_TOKEN,
                null
        )
    }

    fun saveFirebaseToken(token: String?) {
        preferences.edit().putString(
                KEY_FIREBASE_TOKEN,
                token
        ).apply()
    }


}