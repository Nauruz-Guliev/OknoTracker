package ru.kpfu.itis.features.attachments.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kpfu.itis.common.model.ErrorDto

@Serializable
data class AttachmentSingleResponse(
    @SerialName("data")
    val data: AttachmentDto? = null,
    @SerialName("isSuccess")
    val isSuccess: Boolean,
    @SerialName("error")
    val error: ErrorDto? = null
)

@Serializable
data class AttachmentDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("contentType")
    val contentType: String,
    @SerialName("taskId")
    val taskId: Long,
    @SerialName("content")
    val content: String? = null,
)