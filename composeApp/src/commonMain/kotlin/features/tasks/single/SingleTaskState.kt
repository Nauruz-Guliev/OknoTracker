package features.tasks.single

import androidx.compose.runtime.Immutable
import dev.icerock.moko.resources.StringResource
import pro.respawn.flowmvi.api.MVIState
import ru.kpfu.itis.common.model.ErrorModel
import ru.kpfu.itis.features.task.domain.model.TaskModel
import utils.validation.InputField

@Immutable
sealed interface SingleTaskState : MVIState {

    fun findFieldError(field: InputField): StringResource? =
        (this as? Error.Validation)?.fieldErrors?.find { it == field }?.errorText

    data object Initial : SingleTaskState

    data class Success(
        val taskModel: TaskModel,
    ) : SingleTaskState

    sealed interface Error : SingleTaskState {

        data class Validation(val fieldErrors: List<InputField>) : Error
        data class Server(val errorModel: ErrorModel) : Error
        data class Internal(val errorModel: ErrorModel) : Error
    }

    data object Loading : SingleTaskState
}

enum class DialogType {
    DATE, FILE_PICKER, PRIORITY, NONE
}