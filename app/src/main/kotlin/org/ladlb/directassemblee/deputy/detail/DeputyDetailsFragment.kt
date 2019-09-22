package org.ladlb.directassemblee.deputy.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_deputy_details.*
import org.ladlb.directassemblee.AbstractFragment
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.declaration.Declaration
import org.ladlb.directassemblee.declaration.DeclarationBottomSheetDialogFragment
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsHelper
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys
import org.ladlb.directassemblee.firebase.FirebaseAnalyticsKeys.Event
import org.ladlb.directassemblee.helper.NavigationHelper

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

class DeputyDetailsFragment : AbstractFragment(), DeputyContactItemView.DeputyContactItemViewListener, DeputyDeclarationItemView.DeputyDeclarationItemViewListener {

    override fun getClassName(): String = "DeputyDetailsFragment"

    private lateinit var deputy: Deputy

    companion object {

        private const val ARG_DEPUTY: String = "ARG_DEPUTY"

        fun newInstance(deputy: Deputy): DeputyDetailsFragment {

            val bundle = Bundle()
            bundle.putParcelable(ARG_DEPUTY, deputy)

            val fragment = DeputyDetailsFragment()
            fragment.arguments = bundle

            return fragment

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_deputy_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDeputy(arguments!!.getParcelable(ARG_DEPUTY)!!)
    }

    fun setDeputy(deputy: Deputy) {

        this.deputy = deputy

        deputyContact.listener = this
        deputyDeclarations.listener = this

        deputyAbout.setDeputy(this.deputy)
        deputyContact.setDeputy(this.deputy)
        deputyDeclarations.setDeputy(this.deputy)
        deputyRoles.setDeputy(this.deputy)
        deputyMandate.setDeputy(this.deputy)

    }

    override fun onDeputyPhoneClicked(phone: String) {
        firebaseAnalyticsManager.logEvent(
                Event.CALL_DEPUTY,
                FirebaseAnalyticsHelper.addDeputy(
                        Bundle(),
                        deputy
                )
        )

        NavigationHelper.openPhone(
                context!!,
                phone
        )
    }

    override fun onDeputyEmailClicked(email: String) {
        firebaseAnalyticsManager.logEvent(
                Event.SEND_EMAIL_DEPUTY,
                FirebaseAnalyticsHelper.addDeputy(
                        Bundle(),
                        deputy
                )
        )

        NavigationHelper.openMail(
                context!!,
                email
        )
    }

    override fun onDeputyDeclarationClicked(declaration: Declaration) {
        val bundle = FirebaseAnalyticsHelper.addDeputy(
                Bundle(),
                deputy
        )
        bundle.putString(
                FirebaseAnalyticsKeys.ItemKey.DECLARATION_URL,
                declaration.url
        )
        firebaseAnalyticsManager.logEvent(
                Event.DISPLAY_DEPUTY_DECLARATION,
                bundle
        )

        NavigationHelper.openURL(
                context!!,
                declaration.url
        )
    }

    override fun onDeputyMoreDeclarationsClicked(deputy: Deputy) {
        DeclarationBottomSheetDialogFragment.newInstance(deputy).show(
                requireFragmentManager(),
                DeclarationBottomSheetDialogFragment.TAG
        )
    }

}
