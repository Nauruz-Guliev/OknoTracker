package ru.kpfu.itis.di

import org.koin.dsl.module
import ru.kpfu.itis.features.notifications.CommonNotificationManager
import ru.kpfu.itis.features.notifications.CommonNotificationsScheduler
import ru.kpfu.itis.features.notifications.commonNotificationManager
import ru.kpfu.itis.features.notifications.commonNotificationScheduler

expect fun elapsedRealtimeProvider(): Long

fun platformModule() = module {

    single<CommonNotificationsScheduler> { commonNotificationScheduler() }

    single<CommonNotificationManager> { commonNotificationManager() }
}