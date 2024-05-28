package ru.kpfu.itis.features.task.domain.model

class DayStatistics(
    val date: String,
    val tasksCount: Int,
    val completedTasksCount: Int,
    val completedOnTimeTasksCount: Int,
    val remainingTaskCount: Int
)