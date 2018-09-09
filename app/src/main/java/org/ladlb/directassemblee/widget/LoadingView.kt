package org.ladlb.directassemblee.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_loading.view.*
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

class LoadingView : LinearLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        initAttrs(attrs)
    }

    private fun init() {
        inflate(
                context,
                R.layout.view_loading,
                this
        )
    }

    private fun initAttrs(attrs: AttributeSet?) {

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView)

            try {
                val label = typedArray.getString(R.styleable.LoadingView_loadingViewLabel)
                if (!TextUtils.isEmpty(label)) {
                    setLabel(label)
                }
            } finally {
                typedArray.recycle()
            }
        }

    }

    fun setLabel(label: CharSequence) {
        textView.text = label
        progressBar.visibility = View.VISIBLE
    }

    fun setError(label: CharSequence) {
        textView.text = label
        progressBar.visibility = View.INVISIBLE
    }
}