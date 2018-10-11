package org.ladlb.directassemblee.synthesis

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import org.ladlb.directassemblee.AbstractPreferenceFragment
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

class SynthesisFragment : AbstractPreferenceFragment() {

    override fun getClassName(): String = "SynthesisFragment"

    companion object {

        var TAG: String = SynthesisFragment::class.java.name

        fun newInstance(): SynthesisFragment {

            val bundle = Bundle()

            val fragment = SynthesisFragment()
            fragment.arguments = bundle

            return fragment

        }

    }

    private var listener: SynthesisFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            listener = activity as SynthesisFragmentListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement SynthesisFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_synthesis)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findPreference(getString(R.string.preferences_synthesis_rates_by_group)).onPreferenceClickListener =
                Preference.OnPreferenceClickListener { _ ->
                    listener?.onRatesByGroupClicked()
                    return@OnPreferenceClickListener true
                }
    }

    interface SynthesisFragmentListener {

        fun onRatesByGroupClicked()

    }

}