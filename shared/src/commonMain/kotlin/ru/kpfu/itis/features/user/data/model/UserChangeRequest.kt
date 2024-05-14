package ru.kpfu.itis.features.user.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserChangeRequest(
    @SerialName("id")
    val id: Long,
    @SerialName("email")
    val email: String? = null,
    @SerialName("password")
    val password: String? = null,
)