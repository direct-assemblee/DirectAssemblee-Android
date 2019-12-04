package org.ladlb.directassemblee.ballot.vote

import kotlinx.coroutines.launch
import org.ladlb.directassemblee.AbstractPresenter
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.ballot.vote.BallotVoteGetPresenter.BallotVotesGetView
import retrofit2.HttpException
import javax.inject.Inject

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

class BallotVoteGetPresenter @Inject
constructor(val apiRepository: ApiRepository, view: BallotVotesGetView) : AbstractPresenter<BallotVotesGetView>(view) {

    fun getVotes(ballotId: Int) {

        launch {
            try {
                view?.onBallotVotesReceived(apiRepository.getBallotVotes(ballotId))
            } catch (e: Throwable) {
                if (e is HttpException && e.code() == 404) {
                    view?.onNoBallotVotesFound()
                } else {
                    view?.onGetBallotVotesRequestFailed()
                }
            }
        }

    }

    interface BallotVotesGetView : BaseView {

        fun onBallotVotesReceived(votes: BallotVote)

        fun onNoBallotVotesFound()

        fun onGetBallotVotesRequestFailed()

    }

}
