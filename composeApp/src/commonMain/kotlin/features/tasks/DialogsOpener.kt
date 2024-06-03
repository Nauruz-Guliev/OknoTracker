package features.tasks

import androidx.compose.runtime.Immutable
import io.github.vinceglb.picker.core.PlatformFile
import kotlinx.datetime.Instant

@Immutable
sealed interface DialogsOpener {
    data object None : DialogsOpener
    data class DatePicker(val onDatePickedAction: (Instant) -> Unit) : DialogsOpener
    data class FilePicker(val onFilePickedAction: (PlatformFile?) -> Unit) : DialogsOpener
    data class PriorityPicker(
        val priority: TaskPriority,
        val onPriorityPickedAction: (TaskPriority) -> Unit,
    ) : DialogsOpener

    data class SnackBar(val message: String) : DialogsOpener
}
