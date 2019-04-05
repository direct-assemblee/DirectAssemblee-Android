package org.ladlb.directassemblee

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.ladlb.directassemblee.address.SearchAddressActivity
import org.ladlb.directassemblee.address.SearchFragmentProvider
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
import org.ladlb.directassemblee.settings.SettingsActivity
import org.ladlb.directassemblee.settings.SettingsFragmentProvider
import org.ladlb.directassemblee.timeline.TimeLineFragmentProvider

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

    @ContributesAndroidInjector(modules = [DashboardActivityModule::class, DeputyFragmentProvider::class, TimeLineFragmentProvider::class, DeputyDetailsFragmentProvider::class])
    internal abstract fun bindDashboardActivity(): DashboardActivity

    @ContributesAndroidInjector(modules = [BallotVoteActivityModule::class])
    internal abstract fun bindBallotVoteActivity(): BallotVoteActivity

    @ContributesAndroidInjector
    internal abstract fun bindDeputyActivity(): DeputyActivity

    @ContributesAndroidInjector(modules = [SettingsFragmentProvider::class])
    internal abstract fun bindSettingsActivity(): SettingsActivity

}