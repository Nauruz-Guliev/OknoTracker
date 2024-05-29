package features.settings

import pro.respawn.flowmvi.api.MVIState
import ru.kpfu.itis.common.model.ErrorModel
import ru.kpfu.itis.features.settings.domain.SettingKey

sealed interface SettingsState : MVIState {
    data class SettingsDisplay(
        val settings: List<UiSettingItem>
    ) : SettingsState

    data object Loading : SettingsState
    data class Error(val error: ErrorModel) : SettingsState
    data object Initial : SettingsState
}

class UiSettingItem(
    val key: SettingKey,
    val title: String,
    val isChecked: Boolean
)