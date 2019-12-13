package org.ladlb.directassemblee.core.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.widget.AppCompatTextView
import org.ladlb.directassemblee.core.helper.NavigationHelper

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

class UrlTextView : AppCompatTextView, OnClickListener {

    private var url: String? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        visibility = View.GONE
        super.setOnClickListener(this)
    }

    fun setURL(url: String?) {

        if (TextUtils.isEmpty(url)) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            this.url = url
        }

    }

    private var onClickListener: OnClickListener? = null

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        this.onClickListener = onClickListener
    }

    override fun onClick(v: View?) {
        if (onClickListener != null) {
            onClickListener!!.onClick(v)
        }
        NavigationHelper.openURL(
                context,
                url
        )
    }

}
