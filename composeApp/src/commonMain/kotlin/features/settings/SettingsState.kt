package features.settings

import ru.kpfu.itis.features.settings.domain.SettingKey

data class SettingsState(
    val settings: List<UiSettingItem>
)

class UiSettingItem(
    val key: SettingKey,
    val title: String,
    val isChecked: Boolean
)