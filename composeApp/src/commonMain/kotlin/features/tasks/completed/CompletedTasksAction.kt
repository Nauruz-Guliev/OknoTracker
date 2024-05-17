package features.tasks.completed

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction
import ru.kpfu.itis.common.model.ErrorModel

@Immutable
sealed interface CompletedTasksAction : MVIAction {

    data class OpenTaskBottomSheet(val taskId: Long? = null) : CompletedTasksAction
    data class OpenErrorScreen(val errorModel: ErrorModel) : CompletedTasksAction
    data class ShowSnackbar(val message: String) : CompletedTasksAction
    data object Logout : CompletedTasksAction

}