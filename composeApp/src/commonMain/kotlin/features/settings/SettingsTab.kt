package features.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import design_system.screens.OErrorScreen
import design_system.screens.OLoadingScreen
import extensions.startFlowMvi
import features.OTrackerState
import org.koin.compose.koinInject
import pro.respawn.flowmvi.compose.dsl.subscribe

object SettingsTab : Tab {

    @Composable
    override fun Content(): Unit = with(koinInject<SettingsContainer>().store) {

        startFlowMvi()

        val navigator = LocalNavigator.current
        val isNotificationEnabled by rememberSaveable { mutableStateOf(false) }
        val isDarkModeEnabled by rememberSaveable { mutableStateOf(false) }

        val state by subscribe { action ->
            when (action) {
                is SettingsAction.Logout -> {
                    intent(SettingsIntent.Logout)
                }

                is SettingsAction.ToggleDarkTheme -> {
                    intent(SettingsIntent.ToggleDarkMode(isDarkModeEnabled))
                }
            }
        }

        SettingsContent(
            notificationSwitchAction = {
                // todo some sort of storage with booleans should be implemented
            },
            isNotificationEnabled = isNotificationEnabled,
            darkModeSwitchAction = {
                // todo some sort of storage with booleans should be implemented
            },
            isDarkModeEnabled = isDarkModeEnabled
        )

        when (state) {
            is OTrackerState.Success, is OTrackerState.Initial -> {}
            is OTrackerState.Error -> {
                OErrorScreen(
                    errorModel = (state as OTrackerState.Error).error,
                    onClickAction = {
                        // todo
                    }
                )
            }
            is OTrackerState.Loading -> {
                OLoadingScreen()
            }
        }
    }

    @Composable
    private fun SettingsContent(
        isNotificationEnabled: Boolean,
        notificationSwitchAction: (Boolean) -> Unit,
        isDarkModeEnabled: Boolean,
        darkModeSwitchAction: (Boolean) -> Unit,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Notifications", modifier = Modifier.weight(1f))
                Switch(
                    checked = isNotificationEnabled,
                    onCheckedChange = notificationSwitchAction
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Dark Mode", modifier = Modifier.weight(1f))
                Switch(
                    checked = isDarkModeEnabled,
                    onCheckedChange = {
                        darkModeSwitchAction(it)
                    }
                )
            }
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = "Settings"
            val icon = rememberVectorPainter(Icons.Outlined.Settings)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}