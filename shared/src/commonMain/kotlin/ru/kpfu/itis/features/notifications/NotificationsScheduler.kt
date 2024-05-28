package ru.kpfu.itis.features.notifications

interface CommonNotificationsScheduler {
    fun schedule(notificationId: Int, repeatInterval: Long, triggerTime: Long)
    fun removeScheduling(notificationId: Int)
}

expect fun commonNotificationScheduler(): CommonNotificationsScheduler