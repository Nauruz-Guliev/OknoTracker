package features.settings

import ru.kpfu.itis.OResources
import ru.kpfu.itis.features.settings.domain.notificationManager

private const val REMINDER_ID = 992192

class ReminderNotification {
    fun show(): Boolean = notificationManager().showNotification(
        channelId = OResources.Notification.notificationChannelId(),
        notificationTitle = OResources.Notification.notificationContent(),
        notificationId = REMINDER_ID
    )
}