package ru.kpfu.itis.features.task.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TaskPriority {
    @SerialName("Low")
    LOW,

    @SerialName("Medium")
    MEDIUM,

    @SerialName("High")
    HIGH,
}