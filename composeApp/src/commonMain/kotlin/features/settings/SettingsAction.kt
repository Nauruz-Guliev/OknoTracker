package features.settings

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface SettingsAction : MVIAction {

    data class ToggleDarkTheme(val isEnabled: Boolean) : SettingsAction
    data object Logout : SettingsAction
}