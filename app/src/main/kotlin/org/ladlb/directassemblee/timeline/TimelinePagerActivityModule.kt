package org.ladlb.directassemblee.timeline

import dagger.Binds
import dagger.Module
import org.ladlb.directassemblee.timeline.TimelineGetPresenter.TimelineGetView

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
abstract class TimelinePagerActivityModule {

    @Binds
    abstract fun provideTimelineGetView(timelinePagerActivity: TimelinePagerActivity): TimelineGetView

}