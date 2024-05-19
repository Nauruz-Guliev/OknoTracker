package features.tasks.home

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface HomeTasksAction : MVIAction {

    data class OpenTaskBottomSheet(val taskId: Long? = null) : HomeTasksAction
    data class ShowSnackbar(val message: String?) : HomeTasksAction
    data object SignOut : HomeTasksAction
}