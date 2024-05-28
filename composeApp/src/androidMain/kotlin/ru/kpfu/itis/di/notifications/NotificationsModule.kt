package ru.kpfu.itis.di.notifications

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import features.notifications.ReminderNotification
import org.koin.dsl.module
import ru.kpfu.itis.OResources
import ru.kpfu.itis.features.notifications.CommonNotificationManager
import ru.kpfu.itis.features.notifications.NotificationProvider
import ru.kpfu.itis.features.notifications.NotificationScheduler
import ru.kpfu.itis.features.notifications.ONotification

fun notificationsModule() = module {
    single {
        NotificationProvider(
            context = get(),
            notificationsIcon = OResources.Notification.notificationIcon().drawableResId,
            notificationManager = get<Context>().getSystemService(NotificationManager::class.java)
        )
    }

    single {
        listOf<ONotification>(ReminderNotification(get<CommonNotificationManager>()))
    }

    single {
        NotificationScheduler(
            alarmManager = get<Context>().getSystemService(AlarmManager::class.java),
            context = get<Context>()
        )
    }
}