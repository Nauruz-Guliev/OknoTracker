package design_system.chips

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ChipColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import theme.inversePrimaryLight
import theme.onPrimaryDark
import theme.primaryContainerLight

@Composable
fun OChips(
    text: String,
    onClick: () -> Unit = { },
    enabled: Boolean = false,
    icon: @Composable () -> Unit = { }
) {
    AssistChip(
        modifier = Modifier.height(24.dp)
            .padding(2.dp),
        onClick = onClick,
        enabled = enabled,
        label = {
            Text(text, style = MaterialTheme.typography.bodyMedium)
        },
        shape = RoundedCornerShape(32),
        leadingIcon = icon,
        colors = ChipColors(
            containerColor = primaryContainerLight,
            labelColor = MaterialTheme.colorScheme.onPrimary,
            leadingIconContentColor = MaterialTheme.colorScheme.secondary,
            disabledLabelColor = onPrimaryDark,
            disabledLeadingIconContentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = inversePrimaryLight,
            disabledTrailingIconContentColor = MaterialTheme.colorScheme.secondary,
            trailingIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}