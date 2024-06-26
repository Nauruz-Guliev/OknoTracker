package features.tasks.completed.mvi

import pro.respawn.flowmvi.api.MVIIntent

sealed interface CompletedTasksIntent : MVIIntent {

    data object LoadTasks : CompletedTasksIntent
    data class DeleteTask(val taskId: Long) : CompletedTasksIntent
    data class EditTask(val taskId: Long) : CompletedTasksIntent
    data class TaskChecked(val isCompleted: Boolean, val taskId: Long) : CompletedTasksIntent
    data object ClearUserCache : CompletedTasksIntent
}