package features.tasks.main

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction
import ru.kpfu.itis.common.model.ErrorModel

@Immutable
sealed interface MainTasksAction : MVIAction {

    data class OpenTaskBottomSheet(val taskId: Long? = null) : MainTasksAction
    data class OpenErrorScreen(val errorModel: ErrorModel, val userId: Long) : MainTasksAction
    data class ShowSnackbar(val message: String) : MainTasksAction
}