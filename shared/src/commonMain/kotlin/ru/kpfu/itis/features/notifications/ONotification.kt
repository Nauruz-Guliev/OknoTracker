package ru.kpfu.itis.features.notifications

interface ONotification {
    val id: Int

    fun show(): Boolean
}