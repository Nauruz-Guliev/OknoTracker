package ru.kpfu.itis.features.attachments.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttachmentRequest(
    @SerialName("name")
    val name: String,
    @SerialName("contentType")
    val contentType: String,
    @SerialName("taskId")
    val taskId: Long,
    @SerialName("content")
    val content: String,
)