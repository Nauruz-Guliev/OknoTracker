package ru.kpfu.itis.di.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.work.WorkManager
import features.notifications.ReminderNotification
import org.koin.dsl.module
import ru.kpfu.itis.OResources
import ru.kpfu.itis.features.notifications.AndroidNotificationChannelProvider
import ru.kpfu.itis.features.notifications.CommonNotificationManager
import ru.kpfu.itis.features.notifications.CommonNotificationsScheduler
import ru.kpfu.itis.features.notifications.NotificationChannelProvider
import ru.kpfu.itis.features.notifications.NotificationScheduler
import ru.kpfu.itis.features.notifications.ONotification

fun notificationsModule() = module {
    single<NotificationManager> {
        get<Context>().getSystemService(NotificationManager::class.java)
    }

    single<CommonNotificationManager> {
        NotificationChannelProvider(
            context = get<Context>(),
            notificationsIcon = OResources.Notification.notificationIcon().drawableResId,
            notificationManager = get<NotificationManager>()
        )
    }

    // TODO() module in composeApp because of this
    single<ONotification> {
        ReminderNotification(get<CommonNotificationManager>())
    }

    single<CommonNotificationsScheduler> {
        NotificationScheduler(
            workManager = WorkManager.getInstance(get<Context>()),
        )
    }

    single<AndroidNotificationChannelProvider> {
        AndroidNotificationChannelProvider(get<NotificationManager>())
    }
}