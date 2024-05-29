package features.tasks.single

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface SingleTaskAction : MVIAction {

    data class ShowSnackbar(val message: String) : SingleTaskAction
    data object CloseBottomSheet : SingleTaskAction
    data class AddSelectedImage(val image: ByteArray) : SingleTaskAction
    data object OpenDateDialog : SingleTaskAction
    data object OpenPriorityAlert : SingleTaskAction
    data object OpenFilePicker : SingleTaskAction
}
