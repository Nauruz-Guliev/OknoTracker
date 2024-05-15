package ru.kpfu.itis.features.user.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResultDto(
    @SerialName("id")
    val id: Long,
    @SerialName("email")
    val email: String,
)