package org.ladlb.directassemblee.network.da

import okhttp3.ResponseBody
import org.ladlb.directassemblee.model.BallotVote
import org.ladlb.directassemblee.model.Deputy
import org.ladlb.directassemblee.model.Rate
import org.ladlb.directassemblee.model.TimelineItem
import retrofit2.http.*

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

interface ApiServices {

    @Headers("Cache-Control: max-age=3600")
    @GET("deputy")
    suspend fun getDeputyAsync(
            @Query("departmentId") departmentId: Int,
            @Query("district") district: Int
    ): Deputy

    @Headers("Cache-Control: max-age=3600")
    @GET("deputies")
    suspend fun getDeputiesAsync(
            @Query("latitude") latitude: Double,
            @Query("longitude") longitude: Double
    ): List<Deputy>

    @Headers("Cache-Control: max-age=3600")
    @GET("alldeputies")
    suspend fun getAllDeputiesAsync(): List<Deputy>

    @Headers("Cache-Control: max-age=60")
    @GET("timeline")
    suspend fun getTimelineAsync(
            @Query("deputyId") deputyId: Int,
            @Query("page") page: Int
    ): List<TimelineItem>

    @Headers("Cache-Control: max-age=3600")
    @GET("votes")
    suspend fun getBallotVotesAsync(
            @Query("ballotId") ballotId: Int
    ): BallotVote

    @Headers("Cache-Control: max-age=3600")
    @GET("activityRates")
    suspend fun getActivityRatesAsync(): List<Rate>

    @POST("subscribe")
    suspend fun postSubscribeAsync(
            @Body body: Map<String, String>,
            @Query("deputyId") deputyId: Int
    ): ResponseBody

    @POST("unsubscribe")
    suspend fun postUnSubscribeAsync(
            @Body body: Map<String, String>,
            @Query("deputyId") deputyId: Int
    ): ResponseBody

}
