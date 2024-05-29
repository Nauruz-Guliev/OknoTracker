package features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import design_system.button.OButton
import design_system.screens.OErrorScreen
import design_system.screens.OLoadingScreen
import dev.icerock.moko.resources.compose.stringResource
import extensions.startFlowMvi
import features.settings.mvi.SettingsAction
import features.settings.mvi.SettingsContainer
import features.settings.mvi.SettingsIntent
import features.signin.SignInScreen
import org.koin.compose.koinInject
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.OResources
import theme.LocalPaddingValues
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
object SettingsTab : Tab {

    @Composable
    override fun Content(): Unit = with(koinInject<SettingsContainer>().store) {

        startFlowMvi()

        val navigator = LocalNavigator.current
        val scaffoldState = rememberScaffoldState()
        val isTimePickerVisible = remember { mutableStateOf(false) }

        val state by subscribe { action ->
            when (action) {
                is SettingsAction.Logout -> {
                    navigator?.parent?.replace(SignInScreen())
                }

                is SettingsAction.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(action.message)
                }

                SettingsAction.HideTimePicker -> {
                    isTimePickerVisible.value = false
                }

                SettingsAction.ShowTimePicker -> {
                    isTimePickerVisible.value = true
                }
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(bottom = LocalPaddingValues.current.medium)
            ) {
                when (state) {
                    is SettingsState.SettingsDisplay -> SettingsContent(state as SettingsState.SettingsDisplay)
                    SettingsState.Initial -> Unit
                    is SettingsState.Error -> {
                        OErrorScreen(
                            errorModel = (state as SettingsState.Error).error,
                            onClickAction = {
                                intent(SettingsIntent.TryAgain)
                            }
                        )
                    }

                    is SettingsState.Loading -> {
                        OLoadingScreen()
                    }
                }

                OButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    onClickAction = { intent(SettingsIntent.Logout) },
                    text = stringResource(OResources.Settings.logout())
                )

                if (isTimePickerVisible.value) {
                    OTimePickerDialog(
                        onCancel = {
                            intent(SettingsIntent.TimePickerDialogWasClosed)
                        },
                        onConfirm = { hour, minute ->
                            intent(SettingsIntent.NotificationTimeWasSelected(hour, minute))
                        }
                    )
                }
            }
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


    @Composable
    private fun Store<SettingsState, SettingsIntent, SettingsAction>.SettingsContent(
        state: SettingsState.SettingsDisplay
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalPaddingValues.current.medium)
        ) {
            state.settings.forEach { item ->
                item(key = item.key) {
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

    @Composable
    fun OTimePickerDialog(
        title: String = stringResource(OResources.Common.timePickerDialogTitle()),
        onCancel: () -> Unit,
        onConfirm: (hour: Int, minute: Int) -> Unit,
        toggle: @Composable () -> Unit = {},
    ) {
        val time = Calendar.getInstance()
        val state = rememberTimePickerState(
            initialHour =
            time[Calendar.HOUR_OF_DAY],
            initialMinute = time[Calendar.MINUTE],
        )
        Dialog(
            onCancel,
            DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .background(
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colors.surface
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        text = title,
                        style = MaterialTheme.typography.h4
                    )
                    TimePicker(state)
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth()
                    ) {
                        toggle()
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(
                            onClick = onCancel
                        ) { Text(stringResource(OResources.Common.pickerDialogCancelButton())) }
                        TextButton(
                            onClick = {
                                onConfirm(state.hour, state.minute)
                            }
                        ) { Text(stringResource(OResources.Common.pickerDialogConfirmButton())) }
                    }
                }
            }
        }
    }
}