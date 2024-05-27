package features.tasks.completed

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface CompletedTasksAction : MVIAction {

    data class OpenTaskBottomSheet(val taskId: Long? = null) : CompletedTasksAction
    data class ShowSnackbar(val message: String?) : CompletedTasksAction
    data object SignOut : CompletedTasksAction
}
