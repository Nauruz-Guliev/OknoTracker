package features.tasks.main

import pro.respawn.flowmvi.api.MVIIntent
import ru.kpfu.itis.common.model.ErrorModel
import ru.kpfu.itis.features.task.domain.model.TaskModel

sealed interface MainIntent : MVIIntent {

    data class ErrorOccurred(val errorModel: ErrorModel) : MainIntent
    data class CreateTask(val taskModel: TaskModel) : MainIntent

    data object FloatingButtonClicked : MainIntent
}