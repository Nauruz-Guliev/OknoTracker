package features

import pro.respawn.flowmvi.api.MVIState
import ru.kpfu.itis.common.model.ErrorModel

sealed interface OTrackerState<out T> : MVIState {

    data class Error(val error: ErrorModel) : OTrackerState<Nothing>
    data class Success<out T>(val data: T) : OTrackerState<T>
    data object Loading : OTrackerState<Nothing>
    data object Initial : OTrackerState<Nothing>
}