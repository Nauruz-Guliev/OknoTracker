package ru.kpfu.itis.features.task.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskChangeRequest(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("userId")
    val userId: Long,
    @SerialName("deadlineTime")
    val deadlineTime: String,
)