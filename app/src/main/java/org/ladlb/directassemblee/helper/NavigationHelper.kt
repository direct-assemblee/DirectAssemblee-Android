package org.ladlb.directassemblee.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import androidx.browser.customtabs.CustomTabsIntent
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

class NavigationHelper {

    companion object {

        fun openURL(context: Context, url: String?) {
            if (!TextUtils.isEmpty(url)) {
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(ColorHelper.getColorPrimary(context))
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(context, Uri.parse(url))
            }
        }

        fun openPhone(context: Context, phone: String?) {
            if (!TextUtils.isEmpty(phone)) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
        }

        fun openMail(context: Context, mail: String?) {
            if (!TextUtils.isEmpty(mail)) {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
        }

        fun share(context: Context, content: String) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, content)
            intent.type = "text/plain"
            context.startActivity(Intent.createChooser(intent, context.resources.getText(R.string.share_with)))
        }

    }

}
