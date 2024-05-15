package ru.kpfu.itis.features.user.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kpfu.itis.common.data.model.ErrorDto

@Serializable
data class UserResponse(
    @SerialName("data")
    val data: UserResultDto? = null,
    @SerialName("isSuccess")
    val isSuccess: Boolean,
    @SerialName("error")
    val error: ErrorDto? = null,
)