package org.ladlb.directassemblee.ballot.dependencies

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.ladlb.directassemblee.ballot.BallotVoteGetPresenter
import org.ladlb.directassemblee.ballot.BallotVoteRepository
import org.ladlb.directassemblee.ballot.BallotVoteRepositoryImpl

object Dependencies {

    val ballotModule = module {

        single<BallotVoteRepository> { BallotVoteRepositoryImpl(get()) }

        viewModel {
            BallotVoteGetPresenter(get())
        }

    }

}