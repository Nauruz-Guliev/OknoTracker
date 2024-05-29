package features.settings.mvi

import features.settings.UiSettingItem
import pro.respawn.flowmvi.api.MVIIntent

sealed interface SettingsIntent : MVIIntent {

    data object Logout : SettingsIntent
    data object TryAgain : SettingsIntent
    data class UpdateSettingItem(val item: UiSettingItem) : SettingsIntent
    data class NotificationTimeWasSelected(val hour: Int, val minute: Int) : SettingsIntent
    data object TimePickerDialogWasClosed : SettingsIntent
}