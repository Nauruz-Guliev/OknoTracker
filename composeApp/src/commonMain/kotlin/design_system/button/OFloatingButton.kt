package design_system.button

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun OFloatingButton(
    modifier: Modifier = Modifier.size(52.dp),
    icon: ImageVector = Icons.Default.Add,
    onClickedAction: () -> Unit,
) {
    FloatingActionButton(
        onClick = {
            onClickedAction()
        },
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        modifier = modifier,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp,
        ),
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Icon(
            imageVector = icon,
            contentDescription = icon.toString(),
            modifier = Modifier.size(28.dp),
        )
    }
}