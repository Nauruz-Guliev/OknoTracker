package ru.kpfu.itis.features.attachments.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kpfu.itis.common.model.ErrorDto

@Serializable
data class AttachmentMultipleResponse(
    @SerialName("attachmentList")
    val data: List<AttachmentDto> = emptyList(),
    @SerialName("isSuccess")
    val isSuccess: Boolean,
    @SerialName("error")
    val error: ErrorDto? = null
)