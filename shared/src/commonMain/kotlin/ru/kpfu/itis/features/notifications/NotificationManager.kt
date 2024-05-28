package ru.kpfu.itis.features.notifications

import dev.icerock.moko.resources.StringResource

interface CommonNotificationManager {

    fun showNotification(
        channelId: StringResource,
        notificationTitle: StringResource,
        notificationId: Int
    ): Boolean

    fun areNotificationsEnabled(): Boolean
}

expect fun commonNotificationManager(): CommonNotificationManager