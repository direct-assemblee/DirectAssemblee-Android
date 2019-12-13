package org.ladlb.directassemblee.ballot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import org.ladlb.directassemblee.ballot.BallotVoteGetPresenter.BallotVoteGet.*
import org.ladlb.directassemblee.core.AbstractViewModel
import org.ladlb.directassemblee.model.BallotVote
import retrofit2.HttpException

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

class BallotVoteGetPresenter(private val repository: BallotVoteRepository) : AbstractViewModel() {

    sealed class BallotVoteGet {
        class Result(val ballotVote: BallotVote) : BallotVoteGet()
        object NotFound : BallotVoteGet()
        object Error : BallotVoteGet()
    }

    private val _viewState = MutableLiveData<BallotVoteGet>()
    val viewState: LiveData<BallotVoteGet> = _viewState

    fun getVotes(ballotId: Int) {

        launch {
            try {
                _viewState.postValue(Result(repository.getBallotVotes(ballotId)))
            } catch (e: Throwable) {
                if (e is HttpException && e.code() == 404) {
                    _viewState.postValue(NotFound)
                } else {
                    _viewState.postValue(Error)
                }
            }
        }

    }

}
