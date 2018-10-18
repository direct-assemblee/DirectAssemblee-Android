package org.ladlb.directassemblee.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.view_deputy_toolbar.view.*
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.helper.ViewHelper

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

class DeputyToolbar : Toolbar {

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
                R.layout.view_deputy_toolbar,
                this
        )

        ViewHelper.hideView(imageViewDeputy, 0)
        textViewTitle.animate().alpha(0F).duration = 0
        textViewSubTitle.animate().alpha(0F).duration = 0

    }

    fun setDeputy(deputy: Deputy) {

        imageViewDeputy.setDeputyUrl(deputy.photoUrl)
        textViewTitle.text = String.format("%s %s", deputy.firstname, deputy.lastname)
        textViewSubTitle.text = deputy.parliamentGroup

    }

}