package features.settings.mvi

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface SettingsAction : MVIAction {
    data object Logout : SettingsAction
    data class ShowSnackbar(val message: String): SettingsAction
}