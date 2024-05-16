package features.tasks.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun TaskBottomSheet(
    taskId: Long? = null,
    onButtonClickedAction: () -> Unit = {}
) {
    var taskTitle by rememberSaveable { mutableStateOf("") }
    var taskDescription by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {

            OTextField(
                text = taskTitle,
                onValueChange = { },
                label = taskTitle
            )

            OTextField(
                text = taskDescription,
                onValueChange = { },
                label = taskDescription,
                maxLines = 8
            )
        }
        Row(
            modifier = Modifier.weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                onClick = onButtonClickedAction,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.size(56.dp),
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
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