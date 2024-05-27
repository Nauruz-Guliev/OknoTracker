package features.tasks.single

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIIntent
import ru.kpfu.itis.features.task.domain.model.TaskModel

@Immutable
sealed interface SingleTaskIntent : MVIIntent {

    data class LoadTask(val taskId: Long) : SingleTaskIntent
    data class CreateNew(val model: TaskModel) : SingleTaskIntent
    data class Edit(val model: TaskModel) : SingleTaskIntent
    data class Error(val message: String) : SingleTaskIntent
}