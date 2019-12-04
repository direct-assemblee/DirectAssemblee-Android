package org.ladlb.directassemblee.ballot.vote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.ladlb.directassemblee.PresenterTest
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.apiNotFoundException
import org.ladlb.directassemblee.ballot.vote.BallotVoteGetPresenter.BallotVotesGetView
import org.mockito.Mockito.*

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

class BallotVoteGetPresenterTest : PresenterTest() {

    private val presenter: BallotVoteGetPresenter

    private val apiRepository = mock(ApiRepository::class.java)

    private val view: BallotVotesGetView = mock(BallotVotesGetView::class.java)

    init {

        presenter = BallotVoteGetPresenter(apiRepository, view)
        presenter.context = Dispatchers.Unconfined

    }

    @Test
    fun getTimeline_Success() = runBlocking {

        val ballotVote = BallotVote()

        `when`(apiRepository.getBallotVotes(anyInt())).thenReturn(ballotVote)

        presenter.getVotes(anyInt())

        verify(view, atLeastOnce()).onBallotVotesReceived(ballotVote)

    }

    @Test
    fun getTimeline_Fail() = runBlocking {

        `when`(apiRepository.getBallotVotes(anyInt())).thenThrow(NullPointerException())

        presenter.getVotes(anyInt())

        verify(view, atLeastOnce()).onGetBallotVotesRequestFailed()

    }

    @Test
    fun getTimeline_NotFound() = runBlocking {

        `when`(apiRepository.getBallotVotes(anyInt())).thenThrow(apiNotFoundException)

        presenter.getVotes(anyInt())

        verify(view, atLeastOnce()).onNoBallotVotesFound()

    }

}