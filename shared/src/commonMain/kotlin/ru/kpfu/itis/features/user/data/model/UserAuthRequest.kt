package ru.kpfu.itis.features.user.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthRequest(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String,
)