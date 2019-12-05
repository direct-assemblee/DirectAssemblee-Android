package org.ladlb.directassemblee.api.ladlb

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ladlb.directassemblee.ballot.vote.BallotVote
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.rate.Rate
import org.ladlb.directassemblee.timeline.TimelineItem
import javax.inject.Inject
import javax.inject.Singleton
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

@Singleton
class RetrofitApiRepository @Inject
constructor(private val apiServices: ApiServices, private var coroutineContext: CoroutineContext = Dispatchers.IO) : ApiRepository {

    override suspend fun getDeputy(departmentId: Int, district: Int): Deputy = withContext(coroutineContext) {
        apiServices.getDeputyAsync(departmentId, district)
    }

    override suspend fun getDeputies(): Array<Deputy> = withContext(coroutineContext) {
        apiServices.getAllDeputiesAsync()
    }

    override suspend fun getDeputies(latitude: Double, longitude: Double): Array<Deputy> = withContext(coroutineContext) {
        apiServices.getDeputiesAsync(latitude, longitude)
    }

    override suspend fun getTimeline(deputyId: Int, page: Int): Array<TimelineItem> = withContext(coroutineContext) {
        apiServices.getTimelineAsync(deputyId, page)
    }

    override suspend fun getActivityRates(): Array<Rate> = withContext(coroutineContext) {
        apiServices.getActivityRatesAsync()
    }

    override suspend fun getBallotVotes(ballotId: Int): BallotVote = withContext(coroutineContext) {
        apiServices.getBallotVotesAsync(ballotId)
    }

    override suspend fun postSubscribe(id: String, token: String, deputyId: Int) = withContext(coroutineContext) {

        val body = HashMap<String, String>()
        body["instanceId"] = id
        body["token"] = token

        apiServices.postSubscribeAsync(body, deputyId)

        return@withContext

    }

    override suspend fun postUnSubscribe(id: String, token: String, deputyId: Int) = withContext(coroutineContext) {

        val body = HashMap<String, String>()
        body["instanceId"] = id
        body["token"] = token

        apiServices.postUnSubscribeAsync(body, deputyId)

        return@withContext

    }

}
