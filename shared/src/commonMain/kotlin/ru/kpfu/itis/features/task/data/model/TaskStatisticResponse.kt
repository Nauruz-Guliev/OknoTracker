package ru.kpfu.itis.features.task.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kpfu.itis.common.data.model.ErrorDto

@Serializable
data class TaskStatisticResponse(
    @SerialName("taskList")
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