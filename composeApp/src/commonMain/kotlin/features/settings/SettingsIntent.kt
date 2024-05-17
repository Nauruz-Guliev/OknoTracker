package features.settings

import pro.respawn.flowmvi.api.MVIIntent

sealed interface SettingsIntent : MVIIntent {

    data object Logout : SettingsIntent
    data class ToggleDarkMode(val isEnabled: Boolean) : SettingsIntent
}