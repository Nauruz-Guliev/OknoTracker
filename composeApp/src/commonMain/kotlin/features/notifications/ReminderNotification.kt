package features.notifications

import ru.kpfu.itis.OResources
import ru.kpfu.itis.features.notifications.CommonNotificationManager
import ru.kpfu.itis.features.notifications.ONotification

const val REMINDER_NOTIFICATION_ID = 992192

class ReminderNotification(private val commonNotificationManager: CommonNotificationManager) :
    ONotification {
    override val id: Int = REMINDER_NOTIFICATION_ID

    override fun show(): Boolean = commonNotificationManager.showNotification(
        channelId = OResources.Notification.notificationChannelId(),
        notificationTitle = OResources.Notification.notificationContent(),
        notificationId = REMINDER_NOTIFICATION_ID
    )
}