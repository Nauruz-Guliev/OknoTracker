package features.tasks.main

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction
import ru.kpfu.itis.common.model.ErrorModel

@Immutable
sealed interface MainAction : MVIAction {

    data class OpenTaskBottomSheet(val taskId: Long? = null) : MainAction
    data class ShowSnackbar(val message: String) : MainAction
}