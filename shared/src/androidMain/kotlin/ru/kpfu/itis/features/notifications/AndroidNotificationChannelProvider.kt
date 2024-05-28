package ru.kpfu.itis.features.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

fun AndroidNotificationChannelProvider(notificationManager: NotificationManager): AndroidNotificationChannelProvider =
    AndroidNotificationChannelProviderImpl(notificationManager)

interface AndroidNotificationChannelProvider {
    fun createNotificationChannel(
        channelName: String,
        channelDescription: String,
        channelId: String
    )
}

private class AndroidNotificationChannelProviderImpl(private val notificationManager: NotificationManager) :
    AndroidNotificationChannelProvider {

    override fun createNotificationChannel(
        channelName: String,
        channelDescription: String,
        channelId: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

}