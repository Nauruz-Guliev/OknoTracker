package features.tasks

import androidx.compose.runtime.Immutable

@Immutable
sealed interface DialogsOpener {
    data object None : DialogsOpener
    data class TaskBottomSheet(val taskId: Long?) : DialogsOpener
    data class DatePicker(val taskId: Long) : DialogsOpener
    data class FilePicker(val taskId: Long) : DialogsOpener
    data class SnackBar(val message: String) : DialogsOpener
}
