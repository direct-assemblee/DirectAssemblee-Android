package org.ladlb.directassemblee.ballot

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.ladlb.directassemblee.ballot.BallotVoteGetPresenter.BallotVoteGet
import org.ladlb.directassemblee.model.BallotVote
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

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

@Suppress("EXPERIMENTAL_API_USAGE")
class BallotVoteGetPresenterTest {

    @get:Rule
    val mockitoInit = MockitoJUnit.rule()!!

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var presenter: BallotVoteGetPresenter

    @Mock
    private lateinit var ballotVoteRepository: BallotVoteRepository

    @Before
    fun setup() {
        presenter = BallotVoteGetPresenter(ballotVoteRepository)
    }

    @Test
    fun getTimeline_Success() = testCoroutineRule.runBlockingTest {

        given(ballotVoteRepository.getBallotVotes(anyInt())).willReturn(BallotVote())

        presenter.getVotes(anyInt())

        assert(presenter.viewState.value is BallotVoteGet.Result)

    }

    @Test
    fun getTimeline_Fail() = testCoroutineRule.runBlockingTest {

        given(ballotVoteRepository.getBallotVotes(anyInt())).willThrow(NullPointerException())

        presenter.getVotes(anyInt())

        assert(presenter.viewState.value is BallotVoteGet.Error)

    }

    @Test
    fun getTimeline_NotFound() = testCoroutineRule.runBlockingTest {

        given(ballotVoteRepository.getBallotVotes(anyInt())).willThrow(apiNotFoundException)

        presenter.getVotes(anyInt())

        assert(presenter.viewState.value is BallotVoteGet.NotFound)

    }

}