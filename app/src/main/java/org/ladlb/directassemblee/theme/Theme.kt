package org.ladlb.directassemblee.theme

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.helper.parcelableCreator

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

data class Theme(val id: Int = 0, val name: String? = null, val fullName: String? = null,
                 val shortName: String? = null) : Parcelable {

    /**
     * The creator.
     */
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR = parcelableCreator(::Theme)
    }

    /**
     * Constructor.
     *
     * @param source the source.
     */
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    /**
     * Return the label.
     *
     * @return The short name if it's not empty or the full name if it's not empty or finally the name.
     */
    @Nullable
    fun getLabel(): String? = if (TextUtils.isEmpty(shortName)) (if (TextUtils.isEmpty(fullName)) name else fullName) else shortName

    /**
     * Return the drawableId.
     *
     * @return The drawableId associated at the theme or at least the uncategorized drawableId.
     */
    @NonNull
    @DrawableRes
    fun getDrawableId(): Int {

        when (id) {
            1 -> return R.drawable.ic_foreign_affairs_and_cooperation_24dp
            2 -> return R.drawable.ic_agriculture_fisheries_24dp
            3 -> return R.drawable.ic_territory_development_24dp
            4 -> return R.drawable.ic_veterans_24dp
            5 -> return R.drawable.ic_budget_24dp
            6 -> return R.drawable.ic_local_authorities_24dp
            7 -> return R.drawable.ic_culture_24dp
            8 -> return R.drawable.ic_defense_24dp
            9 -> return R.drawable.ic_economy_finance_taxation_24dp
            10 -> return R.drawable.ic_education_24dp
            11 -> return R.drawable.ic_energy_24dp
            12 -> return R.drawable.ic_companies_24dp
            13 -> return R.drawable.ic_environment_24dp
            14 -> return R.drawable.ic_family_24dp
            15 -> return R.drawable.ic_public_function_24dp
            16 -> return R.drawable.ic_justice_24dp
            17 -> return R.drawable.ic_housing_urbanism_24dp
            18 -> return R.drawable.ic_overseas_24dp
            19 -> return R.drawable.ic_pme_trade_craft_24dp
            20 -> return R.drawable.ic_police_security_24dp
            21 -> return R.drawable.ic_public_powers_constitution_24dp
            22 -> return R.drawable.ic_health_24dp
            23 -> return R.drawable.ic_science_24dp
            24 -> return R.drawable.ic_social_services_24dp
            25 -> return R.drawable.ic_society_24dp
            26 -> return R.drawable.ic_sports_24dp
            27 -> return R.drawable.ic_treaties_conventions_24dp
            28 -> return R.drawable.ic_transports_24dp
            29 -> return R.drawable.ic_work_24dp
            30 -> return R.drawable.ic_european_union_24dp
            31 -> return R.drawable.ic_general_policy_24dp
            else -> return R.drawable.ic_uncategorized_24dp
        }

    }

    /**
     * {@inheritDoc}
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(fullName)
        parcel.writeString(shortName)
    }

    /**
     * {@inheritDoc}
     */
    override fun describeContents(): Int = 0
}