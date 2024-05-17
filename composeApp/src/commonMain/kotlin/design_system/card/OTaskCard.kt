package design_system.card

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import design_system.chips.OChips

@Composable
fun OTaskCard(
    onCheckedAction: (Boolean) -> Unit = { },
    labels: List<String> = listOf(),
    title: String
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        )
    ) {
        Row(
            modifier = Modifier.padding(end = 16.dp, start = 8.dp, top = 8.dp, bottom = 8.dp)

        ) {
            Checkbox(
                checked = false,
                onCheckedChange = onCheckedAction,
            )
            Column(
                modifier = Modifier.weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = title,
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
                                text = it,
                            )
                        }
                    }
                }
            }
        }
    }
}