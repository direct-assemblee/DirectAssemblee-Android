package org.ladlb.directassemblee.deputy.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.view_deputy_toolbar.view.*
import org.ladlb.directassemblee.core.helper.ViewHelper
import org.ladlb.directassemblee.deputy.R
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

class DeputyToolbar : Toolbar {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {

        inflate(
                context,
                R.layout.view_deputy_toolbar,
                this
        )

        ViewHelper.hideView(imageViewToolbarDeputy, 0)
        textViewToolbarTitle.animate().alpha(0F).duration = 0
        textViewToolbarSubTitle.animate().alpha(0F).duration = 0

    }

    fun setDeputy(deputy: Deputy) {

        imageViewToolbarDeputy.setDeputyUrl(deputy.photoUrl)
        textViewToolbarTitle.text = String.format("%s %s", deputy.firstname, deputy.lastname)
        textViewToolbarSubTitle.text = deputy.parliamentGroup

    }

}
