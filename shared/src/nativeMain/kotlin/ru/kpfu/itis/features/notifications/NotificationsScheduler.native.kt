package ru.kpfu.itis.features.notifications

actual fun commonNotificationScheduler(): CommonNotificationsScheduler = object : CommonNotificationsScheduler{
    override fun schedule(notificationId: Int, repeatInterval: Long, triggerTime: Long) = Unit
    override fun removeScheduling(notificationId: Int) = Unit
    // TODO("notifications")
}