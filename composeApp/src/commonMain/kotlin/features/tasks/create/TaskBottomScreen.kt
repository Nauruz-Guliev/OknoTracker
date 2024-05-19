package features.tasks.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import design_system.textfield.OTextField
import ru.kpfu.itis.features.task.domain.model.TaskModel

@Composable
fun TaskBottomSheet(
    taskId: Long? = null,
    taskDataAction: (TaskModel) -> Unit
) {
    var taskTitle by rememberSaveable { mutableStateOf("") }
    var taskDescription by rememberSaveable { mutableStateOf("") }

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
            )

            OTextField(
                text = taskDescription,
                onValueChange = {
                    taskDescription = it
                },
                label = "Enter task description",
                maxLines = 8,
            )
        }
        Row(
            modifier = Modifier.weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                onClick = {
                    taskDataAction(
                        TaskModel(
                            name = taskTitle,
                            description = taskDescription
                        )
                    )
                },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.size(56.dp),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp,
                ),
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Add",
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}