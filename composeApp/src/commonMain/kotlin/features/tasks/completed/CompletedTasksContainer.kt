package features.tasks.completed

import features.OTrackerState
import flow_mvi.DefaultConfigurationFactory
import flow_mvi.configure
import kotlinx.coroutines.flow.Flow
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.dsl.action
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import ru.kpfu.itis.common.mapper.ErrorMapper
import ru.kpfu.itis.features.task.data.repository.TaskRepository
import ru.kpfu.itis.features.task.data.store.UserStore
import ru.kpfu.itis.features.task.domain.model.TaskModel

class CompletedTasksContainer(
    private val errorMapper: ErrorMapper,
    private val repository: TaskRepository,
    private val configurationFactory: DefaultConfigurationFactory,
    private val userStore: UserStore,
) : Container<OTrackerState<Flow<List<TaskModel>>>, CompletedTasksIntent, CompletedTasksAction> {

    override val store: Store<OTrackerState<Flow<List<TaskModel>>>, CompletedTasksIntent, CompletedTasksAction> =
        store(OTrackerState.Initial) {


            configure(configurationFactory, "Tasks")

            recover { exception ->
                updateState {
                    OTrackerState.Error(errorMapper.map(exception = exception))
                }
                null
            }

            reduce { intent ->

                val userId = userStore.getUserId()
                if (userId == null) {
                    action(CompletedTasksAction.SignOut)
                    intent(CompletedTasksIntent.ClearUserCache)
                } else {
                    when (intent) {
                        is CompletedTasksIntent.LoadTasks -> {
                            updateState { OTrackerState.Loading }
                            repository.getCachedCompletedTasks().also {
                                updateState { OTrackerState.Success(it) }
                            }
                        }

                        is CompletedTasksIntent.EditTask -> {
                            action(CompletedTasksAction.OpenTaskBottomSheet(intent.taskId))
                        }

                        is CompletedTasksIntent.DeleteTask -> {
                            runCatching {
                                repository.deleteTask(intent.taskId)?.let {
                                    action(CompletedTasksAction.ShowSnackbar("Task \"${it.name}\" was deleted"))
                                }
                                action()
                            }.onFailure {
                                action(CompletedTasksAction.ShowSnackbar(it.message))
                            }
                        }

                        is CompletedTasksIntent.ClearUserCache -> {
                            userStore.deleteUserId()
                        }

                        is CompletedTasksIntent.TaskChecked -> {
                            try {
                                if (intent.isCompleted) {
                                    repository.markAsUncompleted(intent.taskId)
                                } else {
                                    repository.markAsCompleted(intent.taskId)
                                }
                                intent(CompletedTasksIntent.LoadTasks)
                            } catch (ex: Exception) {
                                ex.message?.let { action(CompletedTasksAction.ShowSnackbar(it)) }
                            }
                        }
                    }
                }
            }
        }
}



