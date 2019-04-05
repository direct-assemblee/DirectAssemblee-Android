package org.ladlb.directassemblee.ballot.vote

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.ladlb.directassemblee.ballot.vote.BallotVoteGetPresenter.BallotVotesGetView

@Module
abstract class BallotVoteActivityModule {

    @Binds
    abstract fun provideBallotVotesGetView(ballotVoteActivity: BallotVoteActivity): BallotVotesGetView

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideBallotVoteGetPresenter(ballotVotesGetView: BallotVotesGetView): BallotVoteGetPresenter {
            return BallotVoteGetPresenter(ballotVotesGetView)
        }

    }

}