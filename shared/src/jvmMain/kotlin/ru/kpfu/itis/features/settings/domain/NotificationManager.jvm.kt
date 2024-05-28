package ru.kpfu.itis.features.settings.domain

import dev.icerock.moko.resources.StringResource

actual fun notificationManager() = object : CommonNotificationManager {
    override fun showNotification(
        channelId: StringResource,
        notificationTitle: StringResource,
        notificationId: Int
    ) :Boolean= false

    override fun areNotificationsEnabled(): Boolean = false

}
// TODO someday i will show this on ios, but not todoy
