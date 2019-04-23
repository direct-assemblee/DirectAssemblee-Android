package org.ladlb.directassemblee

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.ladlb.directassemblee.address.SearchAddressActivity
import org.ladlb.directassemblee.address.SearchFragmentProvider
import org.ladlb.directassemblee.ballot.BallotFragmentProvider
import org.ladlb.directassemblee.ballot.vote.BallotVoteActivity
import org.ladlb.directassemblee.ballot.vote.BallotVoteActivityModule
import org.ladlb.directassemblee.deputy.DeputyActivity
import org.ladlb.directassemblee.deputy.DeputyFragmentProvider
import org.ladlb.directassemblee.deputy.DeputyListFragmentProvider
import org.ladlb.directassemblee.deputy.detail.DeputyDetailsFragmentProvider
import org.ladlb.directassemblee.deputy.find.DeputyFindActivity
import org.ladlb.directassemblee.deputy.find.DeputyFindFragmentProvider
import org.ladlb.directassemblee.deputy.retrieve.DeputyRetrieveActivity
import org.ladlb.directassemblee.deputy.retrieve.DeputyRetrieveActivityModule
import org.ladlb.directassemblee.deputy.retrieve.DeputyRetrieveFragmentProvider
import org.ladlb.directassemblee.deputy.search.DeputySearchActivity
import org.ladlb.directassemblee.deputy.search.DeputySearchActivityModule
import org.ladlb.directassemblee.deputy.search.PrimaryDeputySearchActivity
import org.ladlb.directassemblee.deputy.search.PrimaryDeputySearchActivityModule
import org.ladlb.directassemblee.motion.MotionFragmentProvider
import org.ladlb.directassemblee.rate.RatesFragmentProvider
import org.ladlb.directassemblee.settings.SettingsActivity
import org.ladlb.directassemblee.settings.SettingsFragmentProvider
import org.ladlb.directassemblee.synthesis.SynthesisActivity
import org.ladlb.directassemblee.timeline.TimelineFragmentProvider
import org.ladlb.directassemblee.timeline.TimelinePagerActivity

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
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    internal abstract fun bindTrampolineActivity(): TrampolineActivity

    @ContributesAndroidInjector(modules = [DeputyRetrieveActivityModule::class, DeputyRetrieveFragmentProvider::class])
    internal abstract fun bindDeputyRetrieveActivity(): DeputyRetrieveActivity

    @ContributesAndroidInjector(modules = [DeputySearchActivityModule::class, DeputyListFragmentProvider::class])
    internal abstract fun bindDeputySearchActivity(): DeputySearchActivity

    @ContributesAndroidInjector(modules = [PrimaryDeputySearchActivityModule::class, DeputyListFragmentProvider::class])
    internal abstract fun bindPrimaryDeputySearchActivity(): PrimaryDeputySearchActivity

    @ContributesAndroidInjector(modules = [DeputyFindFragmentProvider::class])
    internal abstract fun bindDeputyFindActivity(): DeputyFindActivity

    @ContributesAndroidInjector(modules = [SearchFragmentProvider::class])
    internal abstract fun bindSearchAddressActivity(): SearchAddressActivity

    @ContributesAndroidInjector(modules = [DashboardActivityModule::class, DeputyFragmentProvider::class, TimelineFragmentProvider::class, DeputyDetailsFragmentProvider::class])
    internal abstract fun bindDashboardActivity(): DashboardActivity

    @ContributesAndroidInjector(modules = [BallotVoteActivityModule::class, DeputyListFragmentProvider::class])
    internal abstract fun bindBallotVoteActivity(): BallotVoteActivity

    @ContributesAndroidInjector(modules = [DeputyFragmentProvider::class, TimelineFragmentProvider::class, DeputyDetailsFragmentProvider::class])
    internal abstract fun bindDeputyActivity(): DeputyActivity

    @ContributesAndroidInjector(modules = [SettingsFragmentProvider::class])
    internal abstract fun bindSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector(modules = [MotionFragmentProvider::class, BallotFragmentProvider::class])
    internal abstract fun bindTimelinePagerActivity(): TimelinePagerActivity

    @ContributesAndroidInjector(modules = [RatesFragmentProvider::class])
    internal abstract fun bindSynthesisActivity(): SynthesisActivity

}