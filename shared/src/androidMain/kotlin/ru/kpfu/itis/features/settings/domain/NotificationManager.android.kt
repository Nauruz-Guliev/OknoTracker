package ru.kpfu.itis.features.settings.domain

import org.koin.java.KoinJavaComponent.inject
import ru.kpfu.itis.NotificationProvider

private val notificationProvider: NotificationProvider by inject(NotificationProvider::class.java)

actual fun notificationManager(): CommonNotificationManager = notificationProvider