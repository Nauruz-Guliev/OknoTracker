package features.tasks.home

import pro.respawn.flowmvi.api.MVIIntent
import ru.kpfu.itis.common.model.ErrorModel

sealed interface HomeTasksIntent : MVIIntent {

    data object LoadAllTasks : HomeTasksIntent
    data object LoadCachedTasks : HomeTasksIntent
    data object ClearUserCache : HomeTasksIntent
    data object LoadActiveTasks : HomeTasksIntent
    data class DeleteTask(val taskId: Long) : HomeTasksIntent
    data class EditTask(val taskId: Long) : HomeTasksIntent
    data class ErrorOccurred(val errorModel: ErrorModel) : HomeTasksIntent
    data object FloatingButtonClicked : HomeTasksIntent
    data class TaskChecked(val isCompleted: Boolean, val taskId: Long) : HomeTasksIntent

}