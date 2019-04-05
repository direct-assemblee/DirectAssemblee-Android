package org.ladlb.directassemblee.settings

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter
import org.ladlb.directassemblee.notification.NotificationSubscribePresenter.NotificationSubscribeView
import org.ladlb.directassemblee.notification.NotificationUnSubscribePresenter
import org.ladlb.directassemblee.notification.NotificationUnSubscribePresenter.NotificationUnSubscribeView

@Module
abstract class SettingsFragmentModule {

    @Binds
    internal abstract fun provideNotificationSubscribeView(settingsFragment: SettingsFragment): NotificationSubscribeView

    @Binds
    internal abstract fun provideNotificationUnSubscribeView(settingsFragment: SettingsFragment): NotificationUnSubscribeView

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideNotificationSubscribePresenter(view: NotificationSubscribeView): NotificationSubscribePresenter {
            return NotificationSubscribePresenter(view)
        }

        @Provides
        @JvmStatic
        internal fun provideNotificationUnSubscribePresenter(view: NotificationUnSubscribeView): NotificationUnSubscribePresenter {
            return NotificationUnSubscribePresenter(view)
        }

    }

}