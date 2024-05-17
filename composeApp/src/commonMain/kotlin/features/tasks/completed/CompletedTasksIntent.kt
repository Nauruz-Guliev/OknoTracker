package features.tasks.completed

import pro.respawn.flowmvi.api.MVIIntent

sealed interface CompletedTasksIntent : MVIIntent {

    data class LoadTasks(val userId: Long) : CompletedTasksIntent
    data class DeleteTask(val taskId: Long) : CompletedTasksIntent
    data class EditTask(val taskId: Long) : CompletedTasksIntent
    data object ClearUserCache : CompletedTasksIntent
}