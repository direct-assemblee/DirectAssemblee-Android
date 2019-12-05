package org.ladlb.directassemblee.api.ladlb

import org.ladlb.directassemblee.ballot.vote.BallotVote
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.rate.Rate
import org.ladlb.directassemblee.timeline.TimelineItem

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

interface ApiRepository {

    suspend fun getDeputy(departmentId: Int, district: Int): Deputy

    suspend fun getDeputies(): List<Deputy>

    suspend fun getDeputies(latitude: Double, longitude: Double): List<Deputy>

    suspend fun getTimeline(deputyId: Int, page: Int): List<TimelineItem>

    suspend fun postSubscribe(id: String, token: String, deputyId: Int)

    suspend fun postUnSubscribe(id: String, token: String, deputyId: Int)

    suspend fun getBallotVotes(ballotId: Int): BallotVote

    suspend fun getActivityRates(): List<Rate>

}
