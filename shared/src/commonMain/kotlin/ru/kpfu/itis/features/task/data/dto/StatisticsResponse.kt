package ru.kpfu.itis.features.task.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class StatisticsResponse(
    @SerialName("countOfCompletedOnTimeTasks")
    val countOfCompletedOnTimeTasks: Int,
    @SerialName("countOfCompletedTasks")
    val countOfCompletedTasks: Int,
    @SerialName("countOfNotCompletedTasks")
    val countOfNotCompletedTasks: Int,
    @SerialName("countOfOverdueTasks")
    val countOfOverdueTasks: Int,
    @SerialName("countOfTasks")
    val countOfTasks: Int,
    @SerialName("date")
    val date: String
)