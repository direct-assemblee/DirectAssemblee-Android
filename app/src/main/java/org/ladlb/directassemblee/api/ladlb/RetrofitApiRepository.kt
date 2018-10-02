package org.ladlb.directassemblee.api.ladlb

import androidx.annotation.WorkerThread
import com.google.gson.GsonBuilder
import io.reactivex.Completable
import io.reactivex.Single
import org.ladlb.directassemblee.api.RetrofitBaseRepository
import org.ladlb.directassemblee.ballot.vote.BallotVote
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.timeline.TimelineItem
import org.ladlb.directassemblee.vote.Vote
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

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

class RetrofitApiRepository(baseUrl: String, cacheDir: File) : RetrofitBaseRepository(cacheDir), ApiRepository {

    private val services: ApiServices

    @WorkerThread
    override fun getDeputies(latitude: Double, longitude: Double): Single<Array<Deputy>> =
            services.getDeputies(latitude, longitude)

    @WorkerThread
    override fun getDeputy(departmentId: Int, district: Int): Single<Deputy> =
            services.getDeputy(departmentId, district)

    @WorkerThread
    override fun getDeputies(): Single<Array<Deputy>> = services.getAllDeputies()

    @WorkerThread
    override fun getTimeline(deputyId: Int, page: Int): Single<Array<TimelineItem>> =
            services.getTimeline(deputyId, page)

    @WorkerThread
    override fun getBallotVotes(ballotId: Int): Single<BallotVote> =
            services.getBallotVotes(ballotId)

    @WorkerThread
    override fun postSubscribe(id: String, token: String, deputyId: Int): Completable {

        val body = HashMap<String, String>()
        body["instanceId"] = id
        body["token"] = token

        return services.postSubscribe(body, deputyId)

    }

    override fun postUnSubscribe(id: String, token: String, deputyId: Int): Completable {

        val body = HashMap<String, String>()
        body["instanceId"] = id
        body["token"] = token

        return services.postUnSubscribe(body, deputyId)

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        services = retrofit.create(ApiServices::class.java)

    }

}
