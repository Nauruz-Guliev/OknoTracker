package ru.kpfu.itis.features.task.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kpfu.itis.common.model.ErrorDto

@Serializable
data class TaskStatisticsResponse(
    @SerialName("data")
    val data: List<TaskStatisticDto>? = null,
    @SerialName("isSuccess")
    val isSuccess: Boolean,
    @SerialName("error")
    val error: ErrorDto? = null,
)

@Serializable
data class TaskStatisticDto(
    @SerialName("date")
    val date: String,
    @SerialName("countOfTasks")
    val countOfTasks: Long,
    @SerialName("countOfNotCompletedTasks")
    val countOfNotCompletedTasks: Long,
    @SerialName("countOfCompletedTasks")
    val countOfCompletedTasks: Long,
    @SerialName("countOfOverdueTasks")
    val countOfOverdueTasks: Long,
    @SerialName("countOfCompletedOnTimeTasks")
    val countOfCompletedOnTimeTasks: Long,
)