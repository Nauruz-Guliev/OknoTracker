package ru.kpfu.itis.features.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import dev.icerock.moko.resources.StringResource
import org.koin.java.KoinJavaComponent.getKoin

actual fun commonNotificationManager(): CommonNotificationManager {
    return getKoin().get<CommonNotificationManager>()
}

class NotificationChannelProvider(
    private val context: Context,
    @DrawableRes
    private val notificationsIcon: Int,
    private val notificationManager: NotificationManager
) : CommonNotificationManager {

    override fun showNotification(
        channelId: StringResource,
        notificationTitle: StringResource,
        notificationId: Int
    ): Boolean {
        if (!notificationManager.areNotificationsEnabled()) return false
        val notification =
            NotificationCompat.Builder(context, context.getString(channelId.resourceId))
                .setSmallIcon(notificationsIcon)
                .setContentTitle(context.getString(notificationTitle.resourceId))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        notificationManager.notify(notificationId, notification)
        return true
    }

    override fun areNotificationsEnabled(): Boolean = notificationManager.areNotificationsEnabled()

}