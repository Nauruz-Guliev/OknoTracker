package features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import design_system.screens.OErrorScreen
import design_system.screens.OLoadingScreen
import dev.icerock.moko.resources.compose.stringResource
import extensions.startFlowMvi
import features.OTrackerState
import features.settings.mvi.SettingsAction
import features.settings.mvi.SettingsContainer
import features.settings.mvi.SettingsIntent
import org.koin.compose.koinInject
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.OResources
import theme.LocalPaddingValues

object SettingsTab : Tab {

    @Composable
    override fun Content(): Unit = with(koinInject<SettingsContainer>().store) {

        startFlowMvi()

        val scaffoldState = rememberScaffoldState()

        val state by subscribe { action ->
            when (action) {
                is SettingsAction.Logout -> {
                    intent(SettingsIntent.Logout)
                }
                is SettingsAction.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(action.message)
                }
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(LocalPaddingValues.current.medium),
            scaffoldState = scaffoldState
        ) {
            when (state) {
                is OTrackerState.Success -> SettingsContent((state as OTrackerState.Success<SettingsState>).data)
                is OTrackerState.Initial -> Unit
                is OTrackerState.Error -> {
                    OErrorScreen(
                        errorModel = (state as OTrackerState.Error).error,
                        onClickAction = {
                            intent(SettingsIntent.TryAgain)
                        }
                    )
                }

                is OTrackerState.Loading -> {
                    OLoadingScreen()
                }
            }
        }
    }

    @Composable
    private fun Store<OTrackerState<SettingsState>, SettingsIntent, SettingsAction>.SettingsContent(
        state: SettingsState
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            state.settings.forEach { item ->
                SettingItem(
                    itemName = item.title,
                    isChecked = item.isChecked,
                    onCheckChange = {
                        intent(SettingsIntent.UpdateSettingItem(item))
                    }
                )
                Spacer(modifier = Modifier.size(LocalPaddingValues.current.medium))
            }
        }
    }

    @Composable
    private fun SettingItem(
        itemName: String,
        isChecked: Boolean,
        onCheckChange: (Boolean) -> Unit
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = itemName,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.h6
            )
            val isSwitchChecked = remember { mutableStateOf(isChecked) }
            Switch(
                checked = isSwitchChecked.value,
                onCheckedChange = {
                    isSwitchChecked.value = it
                    onCheckChange(it)
                }
            )
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(OResources.Settings.title())
            val icon = rememberVectorPainter(Icons.Filled.Settings)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}