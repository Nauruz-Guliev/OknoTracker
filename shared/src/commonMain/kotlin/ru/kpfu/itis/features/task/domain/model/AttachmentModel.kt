package ru.kpfu.itis.features.task.domain.model

data class AttachmentModel(
    val id: Long = -1,
    val name: String,
    val contentType: String,
    val taskId: Long,
    val content: String = "",
)