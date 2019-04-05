package org.ladlb.directassemblee

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter.NotificationSubscribeView

@Module
abstract class DashboardActivityModule {

    @Binds
    internal abstract fun provideNotificationSubscribeView(dashboardActivity: DashboardActivity): NotificationSubscribeView

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideNotificationSubscribePresenter(view: NotificationSubscribeView): NotificationSubscribePresenter {
            return NotificationSubscribePresenter(view)
        }

    }
}