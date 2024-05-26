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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import design_system.button.OFloatingButton
import design_system.textfield.OTextField
import extensions.startFlowMvi
import features.OTrackerState
import org.koin.compose.koinInject
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.features.task.domain.model.TaskModel

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
            SnackbarHost(snackbarHostState)
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
                modifier = Modifier.weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {

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
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                    isEditingMode = !isEditingMode
                }
            }
        }
    }
}