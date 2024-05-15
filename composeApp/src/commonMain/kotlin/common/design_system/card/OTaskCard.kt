package common.design_system.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ChipColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OTaskCard(
    onCheckedAction: (Boolean) -> Unit = { },
    labels: List<String> = listOf(),
    title: String
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
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
                    labels.forEach {
                        SuggestionChip(
                            modifier = Modifier.padding(horizontal = 2.dp)
                                .height(24.dp),
                            enabled = false,
                            onClick = {},
                            label = {
                                Text(it, style = MaterialTheme.typography.bodySmall)
                            },
                            shape = RoundedCornerShape(32),
                            colors = ChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                labelColor = MaterialTheme.colorScheme.onPrimary,
                                leadingIconContentColor = MaterialTheme.colorScheme.secondary,
                                disabledLabelColor = MaterialTheme.colorScheme.onPrimary,
                                disabledLeadingIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.primary,
                                disabledTrailingIconContentColor = MaterialTheme.colorScheme.secondary,
                                trailingIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }
        }
    }
}