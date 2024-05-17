package features.tasks.main

import pro.respawn.flowmvi.api.MVIIntent
import ru.kpfu.itis.common.model.ErrorModel

sealed interface MainTasksIntent : MVIIntent {

    data object LoadAllTasks : MainTasksIntent
    data object ClearUserCache : MainTasksIntent
    data object LoadActiveTasks : MainTasksIntent
    data class DeleteTask(val taskId: Long) : MainTasksIntent
    data class EditTask(val taskId: Long) : MainTasksIntent
    data class ErrorOccurred(val errorModel: ErrorModel) : MainTasksIntent
    data object FloatingButtonClicked : MainTasksIntent
}