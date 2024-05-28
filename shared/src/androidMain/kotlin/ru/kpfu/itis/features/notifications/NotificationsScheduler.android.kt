package ru.kpfu.itis.features.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import org.koin.java.KoinJavaComponent.inject

private val notificationScheduler: NotificationScheduler by inject(NotificationScheduler::class.java)

actual fun commonNotificationScheduler(): CommonNotificationsScheduler = notificationScheduler

class NotificationScheduler(
    private val alarmManager: AlarmManager,
    private val context: Context
) : CommonNotificationsScheduler {

    override fun schedule(notificationId: Int, repeatInterval: Long, triggerTime: Long) {
        val intent = NotificationBroadcastReceiver.createIntent(context, notificationId)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            repeatInterval,
            pendingIntent
        )
    }

    override fun removeScheduling(notificationId: Int) {
        // TODO() stop scheduling
    }
}