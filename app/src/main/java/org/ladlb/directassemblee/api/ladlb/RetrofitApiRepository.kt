package org.ladlb.directassemblee.api.ladlb

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ladlb.directassemblee.api.RetrofitBaseRepository
import org.ladlb.directassemblee.ballot.vote.BallotVote
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.rate.Rate
import org.ladlb.directassemblee.timeline.TimelineItem
import org.ladlb.directassemblee.vote.Vote
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import kotlin.coroutines.CoroutineContext

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

class RetrofitApiRepository(baseUrl: String, cacheDir: File, var context: CoroutineContext = Dispatchers.IO) : RetrofitBaseRepository(cacheDir), ApiRepository {

    private val services: ApiServices

    override suspend fun getDeputy(departmentId: Int, district: Int): Deputy = withContext(context) {
        services.getDeputyAsync(departmentId, district).await()
    }

    override suspend fun getDeputies(): Array<Deputy> = withContext(context) {
        services.getAllDeputiesAsync().await()
    }

    override suspend fun getDeputies(latitude: Double, longitude: Double): Array<Deputy> = withContext(context) {
        services.getDeputiesAsync(latitude, longitude).await()
    }

    override suspend fun getTimeline(deputyId: Int, page: Int): Array<TimelineItem> = withContext(context) {
        services.getTimelineAsync(deputyId, page).await()
    }

    override suspend fun getActivityRates(): Array<Rate> = withContext(context) {
        services.getActivityRatesAsync().await()
    }

    override suspend fun getBallotVotes(ballotId: Int): BallotVote = withContext(context) {
        services.getBallotVotesAsync(ballotId).await()
    }

    override suspend fun postSubscribe(id: String, token: String, deputyId: Int) = withContext(context) {

        val body = HashMap<String, String>()
        body["instanceId"] = id
        body["token"] = token

        services.postSubscribeAsync(body, deputyId).await()

        return@withContext

    }

    override suspend fun postUnSubscribe(id: String, token: String, deputyId: Int) = withContext(context) {

        val body = HashMap<String, String>()
        body["instanceId"] = id
        body["token"] = token

        services.postUnSubscribeAsync(body, deputyId).await()

        return@withContext

    }

    init {

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setPrettyPrinting()
        gsonBuilder.setDateFormat("dd/MM/yy")
        gsonBuilder.registerTypeAdapter(Vote::class.java, VoteDeserializer())

        val gson = gsonBuilder.create()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        services = retrofit.create(ApiServices::class.java)

    }
}
