package ru.kpfu.itis.features.notifications

actual fun commonNotificationScheduler(): CommonNotificationsScheduler =
    object : CommonNotificationsScheduler {
        //TODO("notifications")
        override fun schedule(notificationId: Int, repeatInterval: Long, triggerTime: Long) = Unit
        override fun removeScheduling(notificationId: Int) = Unit
    }