package features.tasks

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import design_system.textfield.OTextField

@Composable
fun TaskBottomSheetContent(
    title: String,
    titleLabel: String,
    onTitleChange: (String) -> Unit,
    description: String,
    descriptionLabel: String,
    onDescriptionChange: (String) -> Unit,
    onButtonClickedAction: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {

            OTextField(
                text = title,
                onValueChange = onTitleChange,
                label = titleLabel
            )

            OTextField(
                text = description,
                onValueChange = onDescriptionChange,
                label = descriptionLabel,
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