package design_system.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import design_system.chips.OChips
import ru.kpfu.itis.features.task.domain.model.TaskModel

@Composable
fun OTaskCard(
    onCheckedAction: (Boolean, Long) -> Unit,
    task: TaskModel,
    labels: List<Pair<String, Color>> = emptyList(),
    onItemClicked: (Long) -> Unit
) {

    var isTaskChecked by remember { mutableStateOf(task.isCompleted) }

    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                onItemClicked(task.id)
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        )
    ) {
        Row(
            modifier = Modifier.padding(end = 16.dp, start = 8.dp, top = 8.dp, bottom = 8.dp)

        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = {
                    isTaskChecked = it
                    onCheckedAction(isTaskChecked, task.id)
                },
            )
            Column(
                modifier = Modifier.weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    LazyRow {
                        items(labels) {
                            OChips(
                                enabled = false,
                                onClick = {},
                                text = it.first,
                                containerColor = it.second
                            )
                        }
                    }
                }
            }
        }
    }
}