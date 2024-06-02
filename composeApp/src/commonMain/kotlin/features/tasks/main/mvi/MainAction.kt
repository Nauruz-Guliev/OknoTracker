package features.tasks.main.mvi

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface MainAction : MVIAction {

    data class OpenTaskBottomSheet(val taskId: Long? = null) : MainAction
    data class ShowSnackbar(val message: String) : MainAction
}