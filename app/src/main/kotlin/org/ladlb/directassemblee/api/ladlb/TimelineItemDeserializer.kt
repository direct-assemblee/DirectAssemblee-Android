package org.ladlb.directassemblee.api.ladlb

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.ladlb.directassemblee.ballot.BallotGroup
import org.ladlb.directassemblee.law.Law
import org.ladlb.directassemblee.timeline.TimelineItem
import org.ladlb.directassemblee.work.Work
import java.lang.reflect.Type

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

class TimelineItemDeserializer : JsonDeserializer<TimelineItem> {

    companion object {

        private const val WORK = "workType"
        private const val LAW = "lawType"

    }

    override fun deserialize(jsonElement: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TimelineItem {

        val jsonObject = jsonElement.asJsonObject

        return when {
            jsonObject?.has(WORK) == true -> context.deserialize(jsonElement, Work::class.java)
            jsonObject?.has(LAW) == true -> context.deserialize(jsonElement, Law::class.java)
            else -> context.deserialize(jsonElement, BallotGroup::class.java)
        }

    }

}