package org.ladlb.directassemblee.ballot.vote

import io.reactivex.Single
import org.junit.Test
import org.ladlb.directassemblee.PresenterTest
import org.ladlb.directassemblee.api.ladlb.ApiRepository
import org.ladlb.directassemblee.apiNotFoundException
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
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

    @Mock
    lateinit var apiRepository: ApiRepository

    @Mock
    lateinit var view: BallotVoteGetPresenter.GetBallotVotesView

    @Test
    fun getTimeline_Success() {

        val result = BallotVote()

        doReturn(Single.just(result)).`when`(apiRepository).getBallotVotes(anyInt())

        BallotVoteGetPresenter(view, null).getVotes(apiRepository, anyInt())
        verify(view, atLeastOnce()).onBallotVotesReceived(result)

    }

    @Test
    fun getTimeline_Fail() {

        doReturn(Single.error<BallotVote>(Throwable())).`when`(apiRepository).getBallotVotes(anyInt())

        BallotVoteGetPresenter(view, null).getVotes(apiRepository, anyInt())
        verify(view, atLeastOnce()).onGetBallotVotesRequestFailed()

    }

    @Test
    fun getTimeline_NotFound() {

        doReturn(Single.error<BallotVote>(apiNotFoundException)).`when`(apiRepository).getBallotVotes(anyInt())

        BallotVoteGetPresenter(view, null).getVotes(apiRepository, anyInt())
        verify(view, atLeastOnce()).onNoBallotVotesFound()

    }

}