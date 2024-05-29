package features

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.util.Locale


enum class TaskPriority(val value: String) {

    LOW("Low"), MEDIUM("Medium"), HIGH("High");

    companion object {
        private val map = entries.associateBy { it.value }
        operator fun get(value: String): TaskPriority {
            return map[value.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }]
                ?: LOW
        }
    }
}

@Composable
fun TaskPriority.mapToColor(): Color {
    return when (this) {
        TaskPriority.LOW -> MaterialTheme.colorScheme.tertiaryContainer
        TaskPriority.MEDIUM -> MaterialTheme.colorScheme.secondaryContainer
        TaskPriority.HIGH -> MaterialTheme.colorScheme.errorContainer
    }
}