package ru.kpfu.itis

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import dev.icerock.moko.resources.StringResource
import ru.kpfu.itis.features.settings.domain.CommonNotificationManager

class NotificationProvider(
    private val context: Context,
    @DrawableRes
    private val notificationsIcon: Int,
    private val notificationManager: NotificationManager
) : CommonNotificationManager {

    fun createNotificationChannel(
        channelName: String,
        channelDescription: String,
        channelId: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, IMPORTANCE_HIGH).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

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