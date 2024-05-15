package ru.kpfu.itis.features.user.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserCreateRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
)