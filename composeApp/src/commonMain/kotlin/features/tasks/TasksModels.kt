package features.tasks

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIState

@Immutable
sealed interface TasksState : MVIState {

    sealed interface AllTasksState : TasksState {

        data class Success(val taskList: List<>)
    }

    sealed interface ClosedTasksState : TasksState

    sealed interface NewTaskState : TasksState
}