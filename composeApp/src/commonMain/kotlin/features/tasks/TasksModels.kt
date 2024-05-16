package features.tasks

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIIntent
import ru.kpfu.itis.features.task.domain.model.TaskModel


@Immutable
sealed interface TasksIntent : MVIIntent {

    sealed interface CreateOrEdit : TasksIntent {

        data class SaveTask(val taskModel: TaskModel) : CreateOrEdit
        data object EnterEditMode : CreateOrEdit
        data object EnterReadMode : CreateOrEdit
    }
}

