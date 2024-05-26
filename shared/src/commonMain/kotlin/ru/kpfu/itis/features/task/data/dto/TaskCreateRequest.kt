package ru.kpfu.itis.features.task.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
"name": "string",
"description": "string",
"userId": 0,
"deadlineTime": "2024-05-14T09:13:49.921Z"
}
 */
@Serializable
data class TaskCreateRequest(
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("userId")
    val userId: Long,
    @SerialName("deadlineTime")
    val deadlineTime: String? = null,
)