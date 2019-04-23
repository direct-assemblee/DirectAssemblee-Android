package org.ladlb.directassemblee.deputy

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.ladlb.directassemblee.deputy.DeputyGetPresenter.DeputyGetView

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

@Module
abstract class DeputyFragmentModule {

    @Binds
    internal abstract fun provideDeputyGetView(deputyFragment: DeputyFragment): DeputyGetView

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideDeputyGetPresenter(view: DeputyGetView): DeputyGetPresenter {
            return DeputyGetPresenter(view)
        }

    }


}