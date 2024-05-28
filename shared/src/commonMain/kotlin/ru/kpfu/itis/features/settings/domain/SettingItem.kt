package ru.kpfu.itis.features.settings.domain

class SettingItem(val key: SettingKey, val isChecked: Boolean, val title: String)

enum class SettingKey {
    NOTIFICATION, DARK_MODE
}