package org.ladlb.directassemblee.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.item_content.view.*
import kotlinx.android.synthetic.main.item_sub.view.*
import kotlinx.android.synthetic.main.item_title.view.*
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

class SubView : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
    }

    init {

        inflate(
                context,
                R.layout.item_sub,
                this
        )

        // Used for accessibility information
        isFocusable = true

        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        setBackgroundResource(outValue.resourceId)
    }

    private fun initAttrs(attrs: AttributeSet?) {

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SubView)

            try {

                val title = typedArray.getString(R.styleable.SubView_subViewTitle)
                setTitle(title)

                val content = typedArray.getString(R.styleable.SubView_subViewContent)
                setContent(content)

                val gravity = typedArray.getInt(R.styleable.SubView_subViewGravity, 0)
                setGravity(gravity)

            } finally {
                typedArray.recycle()
            }
        }

    }

    override fun setGravity(gravity: Int) {
        when (gravity) {
            1 -> super.setGravity(Gravity.CENTER_HORIZONTAL)
            2 -> super.setGravity(Gravity.END)
            else -> super.setGravity(Gravity.START)
        }

    }

    private fun setTitle(title: String?) {
        if (TextUtils.isEmpty(title)) {
            titleTextView.visibility = View.INVISIBLE
        } else {
            titleTextView.visibility = View.VISIBLE
            titleTextView.text = title
        }
    }

    fun setContent(content: String?) {
        if (TextUtils.isEmpty(content)) {
            contentTextView.visibility = View.INVISIBLE
        } else {
            contentTextView.visibility = View.VISIBLE
            contentTextView.text = content
        }
    }

    fun setDrawable(drawable: Drawable) {
        imageView.setImageDrawable(drawable)
        imageView.visibility = View.VISIBLE
    }

    fun getContent(): String = contentTextView.text.toString()

    fun setValues(title: String?, content: String?) {
        setTitle(title)
        setContent(content)
    }

}
