package ru.kpfu.itis.features.settings.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SettingItemDto(
    @SerialName("key")
    val key: String,
    @SerialName("isChecked")
    val isChecked: Boolean,
    @SerialName("title")
    val title: String
)