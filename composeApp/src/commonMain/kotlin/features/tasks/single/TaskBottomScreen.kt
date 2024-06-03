package features.tasks.single

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import base.BaseScreen
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.fetcher.ByteArrayFetcher
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.rememberImagePainter
import design_system.button.OFloatingButton
import design_system.screens.OLoadingScreen
import design_system.textfield.OTextField
import dev.icerock.moko.resources.compose.stringResource
import features.tasks.DialogsOpener
import features.tasks.TaskPriority
import features.tasks.mapToColor
import features.tasks.single.mvi.ImageModel
import features.tasks.single.mvi.SingleTaskAction
import features.tasks.single.mvi.SingleTaskContainer
import features.tasks.single.mvi.SingleTaskIntent
import features.tasks.single.mvi.SingleTaskState
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import pro.respawn.flowmvi.api.Store
import ru.kpfu.itis.OResources
import ru.kpfu.itis.features.task.domain.model.TaskModel
import theme.LocalPaddingValues
import utils.validation.InputField

class SingleTaskScreen(
    private val taskId: Long? = null,
    private val closeAction: () -> Unit,
) : BaseScreen<SingleTaskState, SingleTaskIntent, SingleTaskAction>() {
    @Composable
    override fun Content() = withStore<SingleTaskContainer> {
        val openDialog = remember { mutableStateOf<DialogsOpener>(DialogsOpener.None) }
        Box {
            HandleDialogRequest(
                dialog = openDialog.value,
                onDismissAction = {
                    openDialog.value = DialogsOpener.None
                }
            )
            action = { action ->
                handleAction(action) {
                    openDialog.value = it
                }
            }
            currentState = { state ->
                HandleState(state) {
                    openDialog.value = it
                }
            }
        }
    }
    @Composable
    fun Store<*, SingleTaskIntent, *>.HandleState(
        state: SingleTaskState,
        openDialogAction: (DialogsOpener) -> Unit,
    ) {
        when (state) {
            is SingleTaskState.Success -> {
                InitialScreen(state.taskModel, state, openDialogAction)
            }

            is SingleTaskState.Initial -> {
                OLoadingScreen(
                    modifier = Modifier.height(240.dp).fillMaxWidth()
                )
                intent(SingleTaskIntent.LoadTask(taskId))
            }

            is SingleTaskState.Loading -> {
                OLoadingScreen(
                    modifier = Modifier.height(240.dp).fillMaxWidth()
                )
            }

            is SingleTaskState.ValidationError -> {
                InitialScreen(state.taskModel, state, openDialogAction)
            }
        }
    }

    private fun handleAction(
        action: SingleTaskAction,
        openDialogAction: (DialogsOpener) -> Unit,
    ) {
        when (action) {
            is SingleTaskAction.CloseBottomSheet -> {
                closeAction()
            }

            is SingleTaskAction.ShowSnackbar -> {
                openDialogAction(DialogsOpener.SnackBar(action.message))
            }
        }
    }
    @Composable
    fun Store<*, SingleTaskIntent, *>.InitialScreen(
        taskModel: TaskModel? = null,
        state: SingleTaskState,
        openDialogAction: (DialogsOpener) -> Unit = { },
    ) {
        val taskTitle = remember { mutableStateOf(taskModel?.name.orEmpty()) }
        val taskDescription = remember { mutableStateOf(taskModel?.description.orEmpty()) }
        val isEditingMode = remember { mutableStateOf(false) }
        val pickedPriority = remember { mutableStateOf(TaskPriority.LOW) }
        val pickedDate = remember { mutableStateOf<LocalDateTime?>(null) }
        val listOfImages = mutableStateListOf<ImageModel>()

        pickedPriority.value = TaskPriority[taskModel?.priority.orEmpty()]
        pickedDate.value = taskModel?.deadlineTime?.toLocalDateTime()

        Column {
            TaskTextFields(
                state = state,
                isEditingMode = isEditingMode.value,
                taskTitle = taskTitle.value,
                taskDescription = taskDescription.value,
                onDescriptionChanged = {
                    taskDescription.value = it
                },
                onTitleChanged = {
                    taskTitle.value = it
                }
            )

            Row {
                TaskChips(
                    onDialogOpenerAction = openDialogAction,
                    isEditingMode = isEditingMode.value,
                    pickedPriority = pickedPriority.value,
                    pickedDate = pickedDate.value.toString()
                )

                TaskButtons(
                    isEditingMode = isEditingMode.value,
                    pickedPriority = pickedPriority.value,
                    pickedDate = pickedDate.value,
                    listOfImages = listOfImages,
                    onEditingModeChanged = {
                        isEditingMode.value = it
                    },
                    taskModel = taskModel,
                    openDialogAction = openDialogAction,
                    taskDescription = taskDescription.value,
                    taskTitle = taskTitle.value
                )

                ImageList(listOfImages, isEditingMode)
            }
        }
    }
    @Composable
    fun Store<*, SingleTaskIntent, *>.TaskButtons(
        isEditingMode: Boolean,
        openDialogAction: (DialogsOpener) -> Unit,
        taskModel: TaskModel?,
        taskDescription: String,
        taskTitle: String,
        pickedDate: LocalDateTime?,
        pickedPriority: TaskPriority,
        listOfImages: List<ImageModel>,
        onEditingModeChanged: (Boolean) -> Unit,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = LocalPaddingValues.current.medium),
            horizontalArrangement = Arrangement.End
        ) {
            Spacer(Modifier.weight(1f))
            if (taskId != null && isEditingMode) {
                OFloatingButton(
                    icon = Icons.Default.Add,
                    onClickedAction = {
                        openDialogAction(DialogsOpener.FilePicker { file ->
                            intent(SingleTaskIntent.OnFileSelected(file, taskId))
                        })
                    },
                )
            }
            Spacer(Modifier.width(LocalPaddingValues.current.small))
            OFloatingButton(
                icon = if (isEditingMode) Icons.Default.Done
                else Icons.Default.Lock,
                onClickedAction = {
                    if (isEditingMode) {
                        handleFloatingButtonClick(
                            taskModel,
                            taskDescription,
                            taskTitle,
                            pickedDate.toString(),
                            pickedPriority.value,
                            listOfImages
                        )
                    }
                    onEditingModeChanged(!isEditingMode)
                })
        }
    }
    @Composable
    fun Store<*, SingleTaskIntent, *>.TaskChips(
        onDialogOpenerAction: (DialogsOpener) -> Unit,
        isEditingMode: Boolean,
        pickedDate: String,
        pickedPriority: TaskPriority,
    ) {
        Column(modifier = Modifier.padding(horizontal = LocalPaddingValues.current.medium)) {
            SuggestionChip(
                onClick = {
                    onDialogOpenerAction(DialogsOpener.DatePicker {
                        intent(SingleTaskIntent.OnDateSelected(it))
                    })
                },
                enabled = isEditingMode,
                label = { Text(pickedDate) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(OResources.Common.imageText())
                    )
                }
            )
            SuggestionChip(
                onClick = {
                    onDialogOpenerAction(DialogsOpener.PriorityPicker(pickedPriority) {
                        intent(SingleTaskIntent.OnPrioritySelected(it))
                    })
                },
                enabled = isEditingMode,
                label = {
                    Text(stringResource(OResources.SingleTask.priorityLabel(), pickedPriority))
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = pickedPriority.mapToColor()
                )
            )
        }
    }
    @Composable
    fun TaskTextFields(
        state: SingleTaskState,
        isEditingMode: Boolean,
        taskTitle: String,
        taskDescription: String,
        onTitleChanged: (String) -> Unit,
        onDescriptionChanged: (String) -> Unit,
    ) {
        Column {
            Spacer(modifier = Modifier.height(LocalPaddingValues.current.extraLarge))
            OTextField(
                text = taskTitle,
                onValueChange = onTitleChanged,
                label = stringResource(OResources.SingleTask.fieldNameLabel()),
                isEnabled = isEditingMode,
                characterMaxCount = InputField.NAME.maxLength,
                errorText = state.findFieldError(InputField.NAME)
                    ?.let { stringResource(it) },
            )
            OTextField(
                text = taskDescription,
                onValueChange = onDescriptionChanged,
                label = stringResource(OResources.SingleTask.fieldDescriptionLabel()),
                maxLines = 8,
                isEnabled = isEditingMode,
                characterMaxCount = InputField.DESCRIPTION.maxLength,
                errorText = state.findFieldError(InputField.DESCRIPTION)
                    ?.let { stringResource(it) },
            )
        }
    }

    fun Store<*, SingleTaskIntent, *>.handleFloatingButtonClick(
        taskModel: TaskModel?,
        taskDescription: String,
        taskTitle: String,
        pickedDate: String,
        pickedPriority: String,
        listOfImages: List<ImageModel>,
    ) {
        when (taskModel) {
            null -> {
                intent(
                    SingleTaskIntent.CreateNew(
                        TaskModel(
                            description = taskDescription,
                            name = taskTitle,
                            deadlineTime = pickedDate,
                            priority = pickedPriority
                        )
                    )
                )
            }

            else -> {
                intent(
                    SingleTaskIntent.Edit(
                        taskModel.copy(
                            description = taskDescription,
                            name = taskTitle,
                            deadlineTime = pickedDate,
                            priority = pickedPriority
                        ),
                        attachments = listOfImages
                    )
                )
            }
        }
    }
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Store<*, SingleTaskIntent, *>.ImageList(
        imageList: SnapshotStateList<ImageModel>,
        isEditingMode: MutableState<Boolean>,
    ) {
        ImageLoader {
            components {
                add(ByteArrayFetcher.Factory())
            }
        }
        LazyRow(
            modifier = Modifier.padding(
                horizontal = LocalPaddingValues.current.medium,
                vertical = LocalPaddingValues.current.small
            )
        ) {
            items(imageList) { model ->
                Image(
                    modifier = Modifier.size(120.dp)
                        .padding(horizontal = LocalPaddingValues.current.medium)
                        .combinedClickable(onLongClick = {
                            if (isEditingMode.value) {
                                intent(SingleTaskIntent.DeleteAttachment(model.id))
                            }
                        }, onClick = { }),
                    painter = rememberImagePainter(
                        ImageRequest(data = model.data)
                    ),
                    contentDescription = stringResource(OResources.Common.imageText()),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}