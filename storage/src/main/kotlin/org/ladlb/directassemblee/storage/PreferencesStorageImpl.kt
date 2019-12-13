package org.ladlb.directassemblee.storage

import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.storage.deserializer.DateDeserializer
import org.ladlb.directassemblee.storage.deserializer.DateSerializer
import java.util.*

/**
 * This file is part of DirectAssemblee-Android <https://github.com/direct-assemblee/DirectAssemblee-Android>.
 *
 * DirectAssemblee-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DirectAssemblee-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DirectAssemblee-Android. If not, see <http://www.gnu.org/licenses/>.
 */

class PreferencesStorageImpl(private val preferences: SharedPreferences) : PreferencesStorage {

    companion object {

        private const val KEY_DEPUTY = "KEY_DEPUTY"
        private const val KEY_NOTIFICATION_DIALOG = "KEY_NOTIFICATION_DIALOG"

    }

    private val gson: Gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateSerializer())
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .create()

    override fun loadDeputy(): Deputy? {

        val json = preferences.getString(
                KEY_DEPUTY,
                null
        )

        return if (TextUtils.isEmpty(json)) {
            null
        } else {
            gson.fromJson(json, Deputy::class.java)
        }

    }

    override fun saveDeputy(deputy: Deputy?) {
        preferences.edit().putString(
                KEY_DEPUTY,
                gson.toJson(deputy)
        ).apply()
    }

    override fun setNotificationDialogShowed(isShow: Boolean) {
        preferences.edit().putBoolean(
                KEY_NOTIFICATION_DIALOG,
                isShow
        ).apply()
    }

    override fun isNotificationDialogShowed(): Boolean {
        return preferences.getBoolean(
                KEY_NOTIFICATION_DIALOG,
                false
        )
    }

}
