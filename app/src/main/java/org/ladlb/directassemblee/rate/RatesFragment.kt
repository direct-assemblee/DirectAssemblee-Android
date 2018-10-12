package org.ladlb.directassemblee.rate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_rates.*
import org.ladlb.directassemblee.AbstractFragment
import org.ladlb.directassemblee.R

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

class RatesFragment : AbstractFragment(), RateGetPresenter.RateGetView {

    override fun getClassName(): String = "RatesFragment"

    companion object {

        var TAG: String = RatesFragment::class.java.name

        fun newInstance(): RatesFragment {

            val bundle = Bundle()

            val fragment = RatesFragment()
            fragment.arguments = bundle

            return fragment

        }

    }

    private lateinit var presenter: RateGetPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = RateGetPresenter(
                this,
                lifecycle
        )

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_rates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.addItemDecoration(RateItemDecoration(resources))

        presenter.getActivityRates(getApiServices())
    }

    override fun onActivityRatesReceived(rates: Array<Rate>) {
        recyclerView.adapter = RateAdapter(rates.toCollection(ArrayList()))
        loadingView.visibility = GONE
    }

    override fun onGetActivityRatesRequestFailed() {
        loadingView.setError(getString(R.string.generic_error))
    }

}