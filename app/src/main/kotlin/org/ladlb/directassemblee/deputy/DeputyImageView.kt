package org.ladlb.directassemblee.deputy

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
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

class DeputyImageView : CircleImageView {

    private var deputyPlaceHolderId: Drawable

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        borderWidth = resources.getDimensionPixelSize(R.dimen.border)

        deputyPlaceHolderId = ResourcesCompat.getDrawable(
                resources,
                R.drawable.img_unknow_deputy,
                null
        )!!
    }

    fun setDeputyUrl(url: String?) {

        when {
            TextUtils.isEmpty(url) -> setImageDrawable(
                    deputyPlaceHolderId
            )
            else -> Picasso.get()
                    .load(url)
                    .placeholder(deputyPlaceHolderId)
                    .error(deputyPlaceHolderId)
                    .into(this)
        }

    }

}