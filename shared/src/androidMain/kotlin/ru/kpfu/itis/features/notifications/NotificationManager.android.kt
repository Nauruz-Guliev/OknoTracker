package ru.kpfu.itis.features.notifications

import org.koin.java.KoinJavaComponent.inject

private val notificationProvider: NotificationProvider by inject(NotificationProvider::class.java)

actual fun commonNotificationManager(): CommonNotificationManager = notificationProvider