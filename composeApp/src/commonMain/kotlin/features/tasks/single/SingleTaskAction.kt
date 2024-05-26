package features.tasks.single

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface SingleTaskAction : MVIAction {

    data class ShowSnackbar(val message: String) : SingleTaskAction
    data object CloseBottomSheet : SingleTaskAction
}
