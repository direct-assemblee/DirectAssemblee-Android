package org.ladlb.directassemblee.notification.dependencies

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.ladlb.directassemblee.notification.*

object Dependencies {

    val notificationModule = module {

        single {
            NotificationStorage(get())
        }

        single<NotificationRepository> {
            NotificationRepositoryImpl(get(), get(), get())
        }

        viewModel {
            NotificationSubscribePresenter(get())
        }

        viewModel {
            NotificationUnsubscribePresenter(get(), get(), get())
        }

    }

}
