package ru.kpfu.itis.common.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    @SerialName("title")
    val title: String,
    @SerialName("details")
    val details: String,
)