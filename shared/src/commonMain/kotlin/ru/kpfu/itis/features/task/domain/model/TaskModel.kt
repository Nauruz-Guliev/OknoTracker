package ru.kpfu.itis.features.task.domain.model

data class TaskModel(
    val id: Long,
    val name: String,
    val userId: Long,
    val isCompleted: Boolean,
    val completedOnTime: Boolean? = null,
    val description: String? = null,
    val lastModifiedTime: String? = null,
    val deadlineTime: String? = null,
    val completedTime: String? = null,
)
