package org.ladlb.directassemblee.ballot

import org.ladlb.directassemblee.model.BallotVote

interface BallotVoteRepository {

    suspend fun getBallotVotes(ballotId: Int): BallotVote

}