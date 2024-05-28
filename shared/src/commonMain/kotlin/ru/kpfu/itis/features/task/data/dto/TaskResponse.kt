package ru.kpfu.itis.features.task.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kpfu.itis.common.model.ErrorDto

@Serializable
data class TaskResponseSingle(
    @SerialName("data")
    val data: TaskDto? = null,
    @SerialName("isSuccess")
    val isSuccess: Boolean,
    @SerialName("error")
    val error: ErrorDto? = null
)

@Serializable
data class TaskResponseList(
    @SerialName("data")
    val data: TaskListDto? = null,
    @SerialName("isSuccess")
    val isSuccess: Boolean,
    @SerialName("error")
    val error: ErrorDto? = null
)

@Serializable
data class TaskListDto(
    @SerialName("taskList")
    val taskList: List<TaskDto>? = null,
    @SerialName("totalPagesCount")
    val totalPagesCount: Long
)

@Serializable
data class TaskDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("userId")
    val userId: Long,
    @SerialName("isCompleted")
    val isCompleted: Boolean,
    @SerialName("lastModifiedTime")
    val lastModifiedTime: String? = null,
    @SerialName("deadlineTime")
    val deadlineTime: String? = null,
    @SerialName("completedTime")
    val completedTime: String? = null,
    @SerialName("completedOnTime")
    val completedOnTime: Boolean? = null,
    @SerialName("priority")
    val priority: TaskPriorityDto,
)