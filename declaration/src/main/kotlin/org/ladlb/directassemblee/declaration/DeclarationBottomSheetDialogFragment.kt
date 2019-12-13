package org.ladlb.directassemblee.declaration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_dialog_declaration.view.*
import org.koin.android.ext.android.inject
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsKeys
import org.ladlb.directassemblee.analytics.firebase.FirebaseAnalyticsManager
import org.ladlb.directassemblee.core.AbstractBottomSheetDialogFragment
import org.ladlb.directassemblee.core.helper.NavigationHelper
import org.ladlb.directassemblee.declaration.DeclarationAdapter.OnDeclarationClickListener
import org.ladlb.directassemblee.model.Declaration
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

class DeclarationBottomSheetDialogFragment : AbstractBottomSheetDialogFragment(), OnDeclarationClickListener {

    override fun getClassName(): String = "DeclarationBottomSheetDialogFragment " + deputy.id

    companion object {

        var ARG_DEPUTY: String = "ARG_DEPUTY"

        var TAG: String = DeclarationBottomSheetDialogFragment::class.java.name

        fun newInstance(deputy: Deputy): DeclarationBottomSheetDialogFragment {

            val bundle = Bundle()
            bundle.putParcelable(ARG_DEPUTY, deputy)

            val fragment = DeclarationBottomSheetDialogFragment()
            fragment.arguments = bundle

            return fragment

        }

    }

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by inject()

    private lateinit var adapter: DeclarationAdapter

    private lateinit var deputy: Deputy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deputy = arguments!!.getParcelable(ARG_DEPUTY)!!

        adapter = DeclarationAdapter(context!!)
        adapter.addItems(deputy.declarations)
        adapter.listener = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return View.inflate(
                context,
                R.layout.fragment_dialog_declaration,
                null
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.recyclerView.adapter = adapter
        view.recyclerView.addItemDecoration(DeclarationItemDecoration(resources))

    }

    override fun onDeclarationClicked(declaration: Declaration) {

        val bundle = FirebaseAnalyticsHelper.addDeputy(
                Bundle(),
                deputy
        )
        bundle.putString(
                FirebaseAnalyticsKeys.ItemKey.DECLARATION_URL,
                declaration.url
        )
        firebaseAnalyticsManager.logEvent(
                FirebaseAnalyticsKeys.Event.DISPLAY_DEPUTY_DECLARATION,
                bundle
        )

        NavigationHelper.openURL(
                context!!,
                declaration.url
        )
        dismiss()
    }

}
