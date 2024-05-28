package ru.kpfu.itis

import android.app.NotificationManager
import android.content.Context
import org.koin.dsl.module

fun notificationsModule() = module {
    single {
        NotificationProvider(
            context = get(),
            notificationsIcon = OResources.Notification.notificationIcon().drawableResId,
            notificationManager = get<Context>().getSystemService(NotificationManager::class.java)
        )
    }
}