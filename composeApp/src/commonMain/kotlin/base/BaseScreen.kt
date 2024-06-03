package base

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import design_system.button.ORadioButton
import extensions.startFlowMvi
import features.tasks.DialogsOpener
import features.tasks.TaskPriority
import features.tasks.mapToColor
import io.github.vinceglb.picker.core.Picker
import io.github.vinceglb.picker.core.PickerSelectionMode
import io.github.vinceglb.picker.core.PickerSelectionType
import kotlinx.datetime.Instant
import org.koin.compose.koinInject
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.compose.dsl.subscribe
import theme.LocalPaddingValues

abstract class BaseScreen<S : MVIState, I : MVIIntent, A : MVIAction> : Screen {
    lateinit var container: Container<S, I, A>
    @Composable
    inline fun <reified T : Container<S, I, A>> withStore(block: @Composable StoreHolder<S, I, A>.() -> Unit) {
        container = koinInject<T>().apply {
            store.startFlowMvi()
        }
        val storeHolder = StoreHolder(container.store)
        return storeHolder.block().also {
            storeHolder.apply {
                val state by subscribe {
                    action(it)
                }
                currentState(state)
            }
        }
    }

    class StoreHolder<S : MVIState, I : MVIIntent, A : MVIAction>(
        store: Store<S, I, A>,
    ) : Store<S, I, A> by store {
        lateinit var action: suspend (A) -> Unit
        lateinit var currentState: @Composable (S) -> Unit
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BoxScope.HandleDialogRequest(
            dialog: DialogsOpener,
            onDismissAction: () -> Unit,
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
            val datePickerState = rememberDatePickerState()
            val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }

            when (dialog) {
                is DialogsOpener.SnackBar -> {
                    LaunchedEffect(Unit) {
                        snackbarHostState.showSnackbar(dialog.message)
                    }
                }

                is DialogsOpener.DatePicker -> {
                    DatePickerDialog(
                        onDismissRequest = onDismissAction,
                        confirmButton = {
                            TextButton(
                                enabled = confirmEnabled.value,
                                onClick = {
                                    val instant = Instant.fromEpochMilliseconds(
                                        datePickerState.selectedDateMillis!!
                                    )
                                    dialog.onDatePickedAction(instant)
                                    onDismissAction()
                                },
                                content = {
                                    Text("Choose date")
                                },
                            )
                        },
                        content = {
                            DatePicker(datePickerState)
                        },
                    )
                }

                DialogsOpener.None -> {}

                is DialogsOpener.FilePicker -> {
                    LaunchedEffect(Unit) {
                        val file = Picker.pickFile(
                            type = PickerSelectionType.Image,
                            mode = PickerSelectionMode.Single,
                            title = "Pick an image",
                            initialDirectory = "/"
                        )
                        dialog.onFilePickedAction(file)
                    }
                }

                is DialogsOpener.PriorityPicker -> {
                    @Composable
                    fun PriorityRadioButton(taskPriority: TaskPriority) {
                        ORadioButton(
                            onPrioritySelected = {
                                dialog.onPriorityPickedAction(taskPriority)
                                onDismissAction()
                            },
                            text = taskPriority.name,
                            selected = dialog.priority == taskPriority,
                            color = taskPriority.mapToColor()
                        )
                    }
                    BasicAlertDialog(
                        onDismissRequest = onDismissAction,
                        content = {
                            OutlinedCard(
                                modifier = Modifier.selectableGroup()
                                    .padding(vertical = LocalPaddingValues.current.small),
                            ) {
                                PriorityRadioButton(TaskPriority.LOW)
                                PriorityRadioButton(TaskPriority.MEDIUM)
                                PriorityRadioButton(TaskPriority.HIGH)
                            }
                        }
                    )
                }
            }
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackbarHostState
            )
        }
    }
}

