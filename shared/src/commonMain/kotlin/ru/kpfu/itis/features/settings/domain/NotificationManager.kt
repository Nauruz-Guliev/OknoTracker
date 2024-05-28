package ru.kpfu.itis.features.settings.domain

import dev.icerock.moko.resources.StringResource

interface CommonNotificationManager {

    fun showNotification(
        channelId: StringResource,
        notificationTitle: StringResource,
        notificationId: Int
    ): Boolean

    fun areNotificationsEnabled(): Boolean
}

expect fun notificationManager(): CommonNotificationManager