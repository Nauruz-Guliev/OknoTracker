package features.tasks.single

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.component.fetcher.ByteArrayFetcher
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.rememberImagePainter
import design_system.button.OFloatingButton
import design_system.button.ORadioButton
import design_system.screens.OLoadingScreen
import design_system.textfield.OTextField
import extensions.convertToString
import extensions.isPastTime
import extensions.mapToByteArray
import extensions.startFlowMvi
import features.TaskPriority
import features.fileds.InputField
import features.mapToColor
import io.github.vinceglb.picker.core.Picker
import io.github.vinceglb.picker.core.PickerSelectionMode
import io.github.vinceglb.picker.core.PickerSelectionType
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.koinInject
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.features.task.domain.model.TaskModel

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalResourceApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun TaskBottomSheet(
    taskId: Long? = null,
    closeAction: () -> Unit,
) = with(koinInject<SingleTaskContainer>().store) {
    startFlowMvi()

    var taskModel by remember { mutableStateOf<TaskModel?>(null) }
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var isEditingMode by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
    var pickedDate by remember { mutableStateOf<LocalDateTime?>(null) }
    val pickedPriority = remember { mutableStateOf(TaskPriority[taskModel?.priority.orEmpty()]) }
    val listOfImages = remember { mutableStateListOf<ImageModel>() }
    val openDialog = remember { mutableStateOf(DialogType.NONE) }

    val state by subscribe { action ->
        when (action) {
            is SingleTaskAction.CloseBottomSheet -> {
                closeAction()
            }

            is SingleTaskAction.ShowSnackbar -> {
                snackbarHostState.showSnackbar(action.message)
            }

            is SingleTaskAction.AddSelectedImage -> {
                listOfImages.add(action.image)
            }

            is SingleTaskAction.DeleteAttachment -> {
                listOfImages.removeIf { it.id == action.id }
            }
        }
    }

    Box {
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
                    isEnabled = isEditingMode,
                    characterMaxCount = InputField.NAME.maxLength,
                    errorText = state.findFieldError(InputField.NAME),
                )

                OTextField(
                    text = taskDescription,
                    onValueChange = {
                        taskDescription = it
                    },
                    label = "Enter task description",
                    maxLines = 8,
                    isEnabled = isEditingMode,
                    characterMaxCount = InputField.DESCRIPTION.maxLength,
                    errorText = state.findFieldError(InputField.DESCRIPTION),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Column {
                    SuggestionChip(
                        onClick = {
                            openDialog.value = DialogType.DATE
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

                    SuggestionChip(
                        onClick = {
                            openDialog.value = DialogType.PRIORITY
                        },
                        enabled = isEditingMode,
                        label = {
                            Text("Priority: ${pickedPriority.value}")
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = pickedPriority.value.mapToColor()
                        )
                    )
                }

                Spacer(Modifier.weight(1f))

                if (taskId != null && isEditingMode) {
                    OFloatingButton(
                        icon = Icons.Default.Add,
                        onClickedAction = {
                            openDialog.value = DialogType.FILE_PICKER
                        },
                    )
                }

                Spacer(Modifier.width(8.dp))

                OFloatingButton(
                    icon = if (isEditingMode) Icons.Default.Done
                    else Icons.Default.Lock,
                    onClickedAction = {
                        if (isEditingMode) {
                            when (taskModel) {
                                null -> {
                                    intent(
                                        SingleTaskIntent.CreateNew(
                                            TaskModel(
                                                description = taskDescription,
                                                name = taskTitle,
                                                deadlineTime = pickedDate?.toString(),
                                                priority = pickedPriority.value.name
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
                                                    deadlineTime = pickedDate?.toString(),
                                                    priority = pickedPriority.value.name
                                                ),
                                                listOfImages
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        isEditingMode = !isEditingMode
                    }
                )

                @Composable
                fun PriorityRadioButton(taskPriority: TaskPriority) {
                    ORadioButton(
                        onPrioritySelected = {
                            pickedPriority.value = taskPriority
                            openDialog.value = DialogType.NONE
                        },
                        text = taskPriority.name,
                        selected = pickedPriority.value == taskPriority,
                        color = taskPriority.mapToColor()
                    )
                }

                when (openDialog.value) {
                    DialogType.DATE -> {
                        DatePickerDialog(
                            onDismissRequest = {
                                openDialog.value = DialogType.NONE
                            },
                            confirmButton = {
                                TextButton(
                                    enabled = confirmEnabled.value,
                                    onClick = {
                                        openDialog.value = DialogType.NONE
                                        val instant =
                                            Instant.fromEpochMilliseconds(datePickerState.selectedDateMillis!!)

                                        val date = instant.toLocalDateTime(TimeZone.UTC)
                                        if (date.isPastTime()) {
                                            intent(SingleTaskIntent.UiError("Past time can not be chosen as deadline."))
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

                    DialogType.FILE_PICKER -> {
                        if (taskId != null) {
                            LaunchedEffect(Unit) {
                                val file = Picker.pickFile(
                                    type = PickerSelectionType.Image,
                                    mode = PickerSelectionMode.Single,
                                    title = "Pick an image",
                                    initialDirectory = "/"
                                )
                                openDialog.value = DialogType.NONE
                                intent(SingleTaskIntent.OnFileSelected(file, taskId))
                            }
                        }
                    }

                    DialogType.PRIORITY -> {
                        BasicAlertDialog(
                            onDismissRequest = {
                                openDialog.value = DialogType.NONE
                            },
                            content = {
                                OutlinedCard(
                                    modifier = Modifier.selectableGroup()
                                        .padding(vertical = 8.dp),
                                ) {
                                    PriorityRadioButton(TaskPriority.LOW)
                                    PriorityRadioButton(TaskPriority.MEDIUM)
                                    PriorityRadioButton(TaskPriority.HIGH)
                                }
                            }
                        )
                    }

                    DialogType.NONE -> {}
                }
            }

            SnackbarHost(snackbarHostState)

            LazyRow(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                items(listOfImages) { model ->
                    Image(
                        modifier = Modifier.size(120.dp)
                            .padding(horizontal = 8.dp)
                            .combinedClickable(
                                onLongClick = {
                                    intent(SingleTaskIntent.DeleteAttachment(model.id))
                                },
                                onClick = { }
                            ),
                        painter = rememberImagePainter(
                            ImageRequest(data = model.data) {
                                components {
                                    add(ByteArrayFetcher.Factory())
                                }
                            }
                        ),
                        contentDescription = "Image",
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }

        when (state) {
            is SingleTaskState.Success -> {
                taskModel = (state as SingleTaskState.Success).taskModel
                taskTitle = taskModel?.name.orEmpty()
                taskDescription = taskModel?.description.orEmpty()
                pickedDate = taskModel?.deadlineTime?.let {
                    LocalDateTime.parse(it)
                }
                pickedPriority.value = TaskPriority[taskModel?.priority ?: TaskPriority.LOW.name]
                taskModel?.attachments?.mapToByteArray()?.let {
                    listOfImages.clear()
                    listOfImages.addAll(it)
                }
            }

            is SingleTaskState.Initial -> {
                taskId?.let { intent(SingleTaskIntent.LoadTask(it)) }
            }

            is SingleTaskState.Loading -> {
                OLoadingScreen(
                    modifier = Modifier.height(240.dp)
                        .fillMaxWidth()
                )
            }

            is SingleTaskState.Error.Internal -> {
                LaunchedEffect(Unit) {
                    snackbarHostState.showSnackbar((state as SingleTaskState.Error.Internal).errorModel.title)
                }
            }

            is SingleTaskState.Error.Server -> {
                LaunchedEffect(Unit) {
                    snackbarHostState.showSnackbar((state as SingleTaskState.Error.Server).errorModel.title)
                }
            }

            is SingleTaskState.Error.Validation -> {}
        }
    }
}