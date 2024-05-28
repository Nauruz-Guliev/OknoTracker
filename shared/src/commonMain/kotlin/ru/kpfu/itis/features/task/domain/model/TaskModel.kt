package ru.kpfu.itis.features.task.domain.model


data class TaskModel(
    val id: Long = -1,
    val name: String,
    val userId: Long = -1,
    val isCompleted: Boolean = false,
    val completedOnTime: Boolean? = null,
    val description: String? = null,
    val lastModifiedTime: String? = null,
    val deadlineTime: String? = null,
    val completedTime: String? = null,
    val priority: String,
    val attachments: List<AttachmentModel> = emptyList(),
)
