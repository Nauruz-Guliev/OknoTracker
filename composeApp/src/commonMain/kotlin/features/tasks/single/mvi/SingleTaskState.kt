package features.tasks.single.mvi

import androidx.compose.runtime.Immutable
import dev.icerock.moko.resources.StringResource
import pro.respawn.flowmvi.api.MVIState
import ru.kpfu.itis.features.task.domain.model.TaskModel
import utils.validation.InputField

@Immutable
sealed interface SingleTaskState : MVIState {
    fun findFieldError(field: InputField): StringResource? =
        (this as? ValidationError)?.fieldErrors?.find { it == field }?.errorText

    data object Initial : SingleTaskState
    data class Success(
        val taskModel: TaskModel,
        val images: List<ImageModel>,
    ) : SingleTaskState

    data class ValidationError(
        val taskModel: TaskModel,
        val fieldErrors: List<InputField>,
    ) : SingleTaskState

    data object Loading : SingleTaskState
}