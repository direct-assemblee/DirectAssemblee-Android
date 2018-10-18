package org.ladlb.directassemblee.deputy.detail

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.fragment_deputy_details.view.*
import kotlinx.android.synthetic.main.item_deputy_contact.view.*
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.helper.ClipboardHelper
import org.ladlb.directassemblee.helper.DrawableHelper
import org.ladlb.directassemblee.widget.SubView

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

class DeputyContactItemView : androidx.cardview.widget.CardView {

    var listener: DeputyContactItemViewListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(
                context,
                R.layout.item_deputy_contact,
                this
        )
    }

    fun setDeputy(deputy: Deputy) {

        val phone = deputy.phone
        val email = deputy.email

        val isEmptyPhone = TextUtils.isEmpty(phone)
        val isEmptyEmail = TextUtils.isEmpty(email)

        if (isEmptyEmail && isEmptyPhone) {
            deputyContact.visibility = View.GONE
        } else {

            deputyContact.visibility = View.VISIBLE

            // Phone
            if (isEmptyPhone) {
                subViewPhone.setContent(
                        context.getString(
                                R.string.deputy_details_unspecified
                        )
                )
            } else {

                subViewPhone.setDrawable(
                        DrawableHelper.getDrawableTintByColorId(
                                resources,
                                R.drawable.ic_phone_24dp,
                                R.color.gray
                        )
                )

                subViewPhone.setContent(phone)
                subViewPhone.setOnClickListener { v ->
                    listener?.onDeputyPhoneClicked((v as SubView).getContent())
                }
                subViewPhone.setOnLongClickListener { v ->
                    ClipboardHelper.clipText(
                            context!!,
                            context.getString(R.string.deputy_details_phone),
                            (v as SubView).getContent()
                    )
                    true
                }
            }

            // Email
            if (isEmptyEmail) {
                subViewEmail.setContent(
                        context.getString(
                                R.string.deputy_details_unspecified
                        )
                )
            } else {

                subViewEmail.setDrawable(
                        DrawableHelper.getDrawableTintByColorId(
                                resources,
                                R.drawable.ic_email_24dp,
                                R.color.gray
                        )
                )

                subViewEmail.visibility = View.VISIBLE
                subViewEmail.setContent(email)
                subViewEmail.setOnClickListener { v ->
                    listener?.onDeputyEmailClicked((v as SubView).getContent())
                }
                subViewEmail.setOnLongClickListener { v ->
                    ClipboardHelper.clipText(
                            context!!,
                            context.getString(R.string.email_address),
                            (v as SubView).getContent()
                    )
                    true
                }
            }

        }

    }

    interface DeputyContactItemViewListener {

        fun onDeputyPhoneClicked(phone: String)

        fun onDeputyEmailClicked(email: String)

    }

}