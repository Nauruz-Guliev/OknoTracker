package ru.kpfu.itis.features.notifications

import dev.icerock.moko.resources.StringResource
import ru.kpfu.itis.features.notifications.CommonNotificationManager

actual fun commonNotificationManager() = object : CommonNotificationManager {
    override fun showNotification(
        channelId: StringResource,
        notificationTitle: StringResource,
        notificationId: Int
    ) :Boolean= false

    override fun areNotificationsEnabled(): Boolean = false

}
// TODO someday i will show this on ios, but not todoy
