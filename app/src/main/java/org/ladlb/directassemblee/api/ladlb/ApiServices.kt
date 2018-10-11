package org.ladlb.directassemblee.api.ladlb

import io.reactivex.Completable
import io.reactivex.Single
import org.ladlb.directassemblee.ballot.vote.BallotVote
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.rate.Rate
import org.ladlb.directassemblee.timeline.TimelineItem
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
    fun getDeputy(
            @Query("departmentId") departmentId: Int,
            @Query("district") district: Int
    ): Single<Deputy>

    @Headers("Cache-Control: max-age=3600")
    @GET("deputies")
    fun getDeputies(
            @Query("latitude") latitude: Double,
            @Query("longitude") longitude: Double
    ): Single<Array<Deputy>>

    @Headers("Cache-Control: max-age=3600")
    @GET("alldeputies")
    fun getAllDeputies(): Single<Array<Deputy>>

    @Headers("Cache-Control: max-age=60")
    @GET("timeline")
    fun getTimeline(
            @Query("deputyId") deputyId: Int,
            @Query("page") page: Int
    ): Single<Array<TimelineItem>>

    @Headers("Cache-Control: max-age=3600")
    @GET("votes")
    fun getBallotVotes(
            @Query("ballotId") ballotId: Int
    ): Single<BallotVote>

    @Headers("Cache-Control: max-age=3600")
    @GET("activityRates")
    fun getActivityRates(): Single<Array<Rate>>

    @POST("subscribe")
    fun postSubscribe(
            @Body body: Map<String, String>,
            @Query("deputyId") deputyId: Int
    ): Completable

    @POST("unsubscribe")
    fun postUnSubscribe(
            @Body body: Map<String, String>,
            @Query("deputyId") deputyId: Int
    ): Completable

}
