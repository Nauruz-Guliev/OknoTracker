package ru.kpfu.itis.features.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.java.KoinJavaComponent.inject

class NotificationBroadcastReceiver : BroadcastReceiver() {

    private val notifications: List<ONotification> by inject(List::class.java)

    override fun onReceive(context: Context, intent: Intent) {
        val key = intent.extras?.getInt(NOTIFICATION_ID_KEY) ?: return
        val notification = notifications.find { it.id == key } ?: return
        notification.show()
    }

    companion object {
        private const val NOTIFICATION_ID_KEY = "notificationId"

        fun createIntent(context: Context, notificationId: Int): Intent {
            return Intent(context, NotificationBroadcastReceiver.javaClass).apply {
                putExtra(NOTIFICATION_ID_KEY, notificationId)
            }
        }
    }
}