package org.ladlb.directassemblee.deputy.detail

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.fragment_deputy_details.view.*
import kotlinx.android.synthetic.main.item_deputy_declaration.view.*
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.declaration.Declaration
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.helper.DrawableHelper
import org.ladlb.directassemblee.helper.FormatHelper
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

class DeputyDeclarationItemView : androidx.cardview.widget.CardView {

    var listener: DeputyDeclarationItemViewListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        inflate(
                context,
                R.layout.item_deputy_declaration,
                this
        )
    }

    fun setDeputy(deputy: Deputy) {

        val declarations = deputy.declarations

        if (declarations.isEmpty()) {

            deputyDeclarations.textViewDeclarationHeader.text = resources.getString(R.string.declaration)

            deputyDeclarations.subViewDeclarationFirst.visibility = View.GONE
            deputyDeclarations.subViewDeclarationSecond.visibility = View.GONE
            deputyDeclarations.subViewDeclarationThird.visibility = View.GONE

            deputyDeclarations.moreView.visibility = View.GONE
        } else {
            deputyDeclarations.textViewDeclarationPlaceHolder.visibility = View.GONE

            deputyDeclarations.textViewDeclarationHeader.text = resources.getQuantityString(
                    R.plurals.declaration,
                    declarations.size,
                    declarations.size
            )

            computeDeclarationView(
                    deputyDeclarations.subViewDeclarationFirst,
                    declarations[0]
            )

            if (declarations.size > 1) {
                computeDeclarationView(
                        deputyDeclarations.subViewDeclarationSecond,
                        declarations[1]
                )
            } else {
                deputyDeclarations.subViewDeclarationSecond.visibility = View.GONE
            }

            if (declarations.size > 2) {
                computeDeclarationView(
                        deputyDeclarations.subViewDeclarationThird,
                        declarations[2]
                )
            } else {
                deputyDeclarations.subViewDeclarationThird.visibility = View.GONE
            }

            deputyDeclarations.moreView.tag = deputy
            deputyDeclarations.moreView.visibility = if (declarations.size > 3) View.VISIBLE else View.GONE
            deputyDeclarations.moreView.setOnClickListener { v ->
                listener?.onDeputyMoreDeclarationsClicked(v.tag as Deputy)
            }

        }

    }

    private fun computeDeclarationView(view: SubView, declaration: Declaration) {
        view.visibility = View.VISIBLE
        view.setDrawable(
                DrawableHelper.getDrawableTintByColorId(
                        resources,
                        R.drawable.ic_attachment_24dp,
                        R.color.gray
                )
        )
        view.setValues(
                declaration.title,
                FormatHelper.format(declaration.date, FormatHelper.DAY)
        )
        view.tag = declaration
        view.setOnClickListener { v ->
            listener?.onDeputyDeclarationClicked(v.tag as Declaration)
        }

    }

    interface DeputyDeclarationItemViewListener {

        fun onDeputyDeclarationClicked(declaration: Declaration)

        fun onDeputyMoreDeclarationsClicked(deputy: Deputy)

    }

}