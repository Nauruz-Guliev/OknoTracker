package features.tasks

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIState
import ru.kpfu.itis.common.model.ErrorModel

@Immutable
sealed interface TasksState<out T> : MVIState {

    data class Success<T>(val taskList: T) : TasksState<T>
    data object Initial : TasksState<Nothing>
    data class Error(val errorModel: ErrorModel) : TasksState<Nothing>
    data object Loading : TasksState<Nothing>
}