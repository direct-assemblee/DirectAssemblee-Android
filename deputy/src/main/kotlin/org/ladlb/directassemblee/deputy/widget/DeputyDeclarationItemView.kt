package org.ladlb.directassemblee.deputy.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.item_deputy_declaration.view.*
import org.ladlb.directassemblee.core.helper.DrawableHelper
import org.ladlb.directassemblee.core.helper.FormatHelper
import org.ladlb.directassemblee.deputy.R
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

class DeputyDeclarationItemView : androidx.cardview.widget.CardView {

    var listener: DeputyDeclarationItemViewListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(
                context,
                R.layout.item_deputy_declaration,
                this
        )
    }

    fun setDeputy(deputy: Deputy) {

        val declarations = deputy.declarations

        if (declarations.isEmpty()) {

            textViewDeclarationHeader.text = resources.getString(R.string.declaration)

            subViewDeclarationFirst.visibility = View.GONE
            subViewDeclarationSecond.visibility = View.GONE
            subViewDeclarationThird.visibility = View.GONE

            moreView.visibility = View.GONE
        } else {
            textViewDeclarationPlaceHolder.visibility = View.GONE

            textViewDeclarationHeader.text = resources.getQuantityString(
                    R.plurals.declaration,
                    declarations.size,
                    declarations.size
            )

            computeDeclarationView(
                    subViewDeclarationFirst,
                    declarations[0]
            )

            if (declarations.size > 1) {
                computeDeclarationView(
                        subViewDeclarationSecond,
                        declarations[1]
                )
            } else {
                subViewDeclarationSecond.visibility = View.GONE
            }

            if (declarations.size > 2) {
                computeDeclarationView(
                        subViewDeclarationThird,
                        declarations[2]
                )
            } else {
                subViewDeclarationThird.visibility = View.GONE
            }

            moreView.tag = deputy
            moreView.visibility = if (declarations.size > 3) View.VISIBLE else View.GONE
            moreView.setOnClickListener { v ->
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