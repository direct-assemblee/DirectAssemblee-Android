package org.ladlb.directassemblee.ballot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Filterable
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_ballot_vote.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.ladlb.directassemblee.ballot.BallotVoteGetPresenter.BallotVoteGet.*
import org.ladlb.directassemblee.core.AbstractToolBarActivity
import org.ladlb.directassemblee.core.helper.collect
import org.ladlb.directassemblee.deputy.DeputyListFragment.DeputyListFragmentListener
import org.ladlb.directassemblee.model.BallotVote
import org.ladlb.directassemblee.model.Deputy

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

class BallotVoteActivity : AbstractToolBarActivity(), OnTabSelectedListener, DeputyListFragmentListener {

    override fun getContentView(): Int = R.layout.activity_ballot_vote

    private val ballotVotesGetPresenter: BallotVoteGetPresenter by viewModel()

    private lateinit var adapter: BallotVotePagerAdapter

    companion object Factory {

        var EXTRA_PAGE: String = "ARG_PAGE"

        var EXTRA_BALLOT_ID: String = "EXTRA_BALLOT_ID"

        fun getIntent(context: Context, ballotId: Int, page: Int = 0): Intent {

            val intent = Intent(
                    context,
                    BallotVoteActivity::class.java
            )

            intent.putExtra(EXTRA_PAGE, page)
            intent.putExtra(EXTRA_BALLOT_ID, ballotId)

            return intent

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchView.visibility = View.GONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                applyQuery(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                applyQuery(query)
                return false
            }

            fun applyQuery(query: String) {
                for ((index, fragment) in supportFragmentManager.fragments.withIndex()) {
                    if (fragment is Filterable) {
                        fragment.filter.filter(
                                query
                        ) { count: Int ->
                            tabLayout.getTabAt(index)?.text = adapter.getPageTitle(index, count)
                        }
                    }
                }
            }
        })

        viewPager.offscreenPageLimit = 5

        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(this)

        ballotVotesGetPresenter.viewState.collect(this) {
            when (it) {
                is Result -> onBallotVotesReceived(it.ballotVote)
                is NotFound -> onNoBallotVotesFound()
                is Error -> onGetBallotVotesRequestFailed()
            }
        }

        ballotVotesGetPresenter.getVotes(
                intent.getIntExtra(EXTRA_BALLOT_ID, 0)
        )

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        when (viewPager.currentItem) {
            0, 1, 2, 3, 4 -> (findViewById<RecyclerView>(R.id.recyclerView).layoutManager as LinearLayoutManager).scrollToPosition(0)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {

    }

    override fun onDeputyClicked(deputy: Deputy) {

    }

    private fun onBallotVotesReceived(votes: BallotVote) {

        adapter = BallotVotePagerAdapter(
                this,
                supportFragmentManager,
                votes
        )
        viewPager.adapter = adapter

        viewPager.currentItem = intent.getIntExtra(EXTRA_PAGE, 0)

        loadingView.visibility = View.GONE
        searchView.visibility = View.VISIBLE

    }

    private fun onNoBallotVotesFound() {
        loadingView.setError(getString(org.ladlb.directassemblee.core.R.string.deputy_not_found))
    }

    private fun onGetBallotVotesRequestFailed() {
        loadingView.setError(getString(org.ladlb.directassemblee.core.R.string.get_deputies_request_failed))
    }
}