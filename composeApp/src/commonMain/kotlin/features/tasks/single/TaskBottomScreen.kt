package features.tasks.single

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import design_system.button.OFloatingButton
import design_system.textfield.OTextField
import extensions.convertToString
import extensions.isPastTime
import extensions.startFlowMvi
import features.OTrackerState
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.features.task.domain.model.TaskModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBottomSheet(
    taskId: Long? = null,
    closeAction: () -> Unit
) = with(koinInject<SingleTaskContainer>().store) {
    startFlowMvi()

    var taskModel by remember { mutableStateOf<TaskModel?>(null) }
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var isEditingMode by remember { mutableStateOf(false) }
    var openDateDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
    var pickedDate by remember { mutableStateOf<LocalDateTime?>(null) }

    val state by subscribe { action ->
        when (action) {
            is SingleTaskAction.CloseBottomSheet -> {
                closeAction()
            }

            is SingleTaskAction.ShowSnackbar -> {
                snackbarHostState.showSnackbar(action.message)
            }
        }
    }

    when (state) {
        is OTrackerState.Success -> {
            taskModel = (state as OTrackerState.Success<TaskModel>).data
            taskTitle = taskModel?.name ?: ""
            taskDescription = taskModel?.description ?: ""
            pickedDate = taskModel?.deadlineTime?.let {
                LocalDateTime.parse(it)
            }
        }

        is OTrackerState.Initial -> {
            taskId?.let { intent(SingleTaskIntent.LoadTask(it)) }
        }

        is OTrackerState.Loading -> {}
        is OTrackerState.Error -> {
            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar((state as OTrackerState.Error).error.title)
            }
        }
    }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column {
                Spacer(
                    modifier = Modifier.height(32.dp)
                )

                OTextField(
                    text = taskTitle,
                    onValueChange = {
                        taskTitle = it
                    },
                    label = "Enter task name",
                    isEnabled = isEditingMode
                )

                OTextField(
                    text = taskDescription,
                    onValueChange = {
                        taskDescription = it
                    },
                    label = "Enter task description",
                    maxLines = 8,
                    isEnabled = isEditingMode
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {

                SuggestionChip(
                    onClick = {
                        openDateDialog = true
                    },
                    enabled = isEditingMode,
                    label = {
                        Text(pickedDate?.convertToString() ?: "Deadline")
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date"
                        )
                    }
                )

                Spacer(Modifier.weight(1f))

                OFloatingButton(
                    icon = if (isEditingMode) Icons.Default.Done
                    else Icons.Default.Lock
                ) {
                    if (isEditingMode) {
                        when (taskModel) {
                            null -> {
                                intent(
                                    SingleTaskIntent.CreateNew(
                                        TaskModel(
                                            description = taskDescription,
                                            name = taskTitle,
                                            deadlineTime = pickedDate?.toString()
                                        )
                                    )
                                )
                            }

                            else -> {
                                taskModel?.let {
                                    intent(
                                        SingleTaskIntent.Edit(
                                            it.copy(
                                                description = taskDescription,
                                                name = taskTitle,
                                                deadlineTime = pickedDate?.toString()
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                    isEditingMode = !isEditingMode
                }

                if (openDateDialog) {
                    DatePickerDialog(
                        onDismissRequest = {
                            openDateDialog = false
                        },
                        confirmButton = {
                            TextButton(
                                enabled = confirmEnabled.value,
                                onClick = {
                                    openDateDialog = false
                                    val instant =
                                        Instant.fromEpochMilliseconds(datePickerState.selectedDateMillis!!)

                                    val date = instant.toLocalDateTime(TimeZone.UTC)
                                    if (date.isPastTime()) {
                                        intent(SingleTaskIntent.Error("Past time can not be chosen as deadline."))
                                    } else {
                                        pickedDate = date
                                    }
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
            }
            SnackbarHost(snackbarHostState)
        }
    }
}