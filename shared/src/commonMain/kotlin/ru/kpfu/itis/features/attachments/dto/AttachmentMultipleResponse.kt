package ru.kpfu.itis.features.attachments.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kpfu.itis.common.model.ErrorDto

@Serializable
data class AttachmentMultipleResponse(
    @SerialName("data")
    val data: AttachmentData? = null,
    @SerialName("isSuccess")
    val isSuccess: Boolean,
    @SerialName("error")
    val error: ErrorDto? = null
)

@Serializable
data class AttachmentData(
    @SerialName("attachmentList")
    val data: List<AttachmentDto> = emptyList(),
)