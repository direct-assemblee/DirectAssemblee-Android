package org.ladlb.directassemblee.ballot

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ladlb.directassemblee.model.BallotVote
import org.ladlb.directassemblee.network.da.ApiServices
import kotlin.coroutines.CoroutineContext

class BallotVoteRepositoryImpl(private val apiServices: ApiServices, private var coroutineContext: CoroutineContext = Dispatchers.IO) : BallotVoteRepository {

    override suspend fun getBallotVotes(ballotId: Int): BallotVote = withContext(coroutineContext) {
        apiServices.getBallotVotesAsync(ballotId)
    }

}
