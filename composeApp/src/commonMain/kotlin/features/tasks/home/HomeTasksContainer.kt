package features.tasks.home

import features.OTrackerState
import flow_mvi.DefaultConfigurationFactory
import flow_mvi.configure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import ru.kpfu.itis.common.mapper.ErrorMapper
import ru.kpfu.itis.features.task.data.model.TaskType
import ru.kpfu.itis.features.task.data.repository.TaskRepository
import ru.kpfu.itis.features.task.data.store.UserStore
import ru.kpfu.itis.features.task.domain.model.TaskModel

class HomeTasksContainer(
    private val errorMapper: ErrorMapper,
    private val repository: TaskRepository,
    private val configurationFactory: DefaultConfigurationFactory,
    private val userStore: UserStore,
) : Container<OTrackerState<Flow<List<TaskModel>>>, HomeTasksIntent, HomeTasksAction> {

    override val store: Store<OTrackerState<Flow<List<TaskModel>>>, HomeTasksIntent, HomeTasksAction> =
        store(OTrackerState.Initial) {

            configure(configurationFactory, "Tasks")

            recover { exception ->
                updateState { OTrackerState.Error(errorMapper.map(exception = exception)) }
                null
            }

            reduce { intent ->
                val userId = userStore.getUserId()
                if (userId == null) {
                    action(HomeTasksAction.SignOut)
                    intent(HomeTasksIntent.ClearUserCache)
                } else {
                    when (intent) {
                        is HomeTasksIntent.LoadActiveTasks, is HomeTasksIntent.LoadAllTasks -> {
                            updateState { OTrackerState.Loading }
                            launch { repository.updateTasks(TaskType.ALL, userId) }
                            if (intent is HomeTasksIntent.LoadActiveTasks) {
                                repository.getCachedTasks(TaskType.ACTIVE).also { tasks ->
                                    updateState { OTrackerState.Success(tasks) }
                                }
                            } else {
                                repository.getCachedTasks(TaskType.ALL).also { tasks ->
                                    updateState { OTrackerState.Success(tasks) }
                                }
                            }
                        }

                        is HomeTasksIntent.EditTask -> {
                            action(HomeTasksAction.OpenTaskBottomSheet(intent.taskId))
                        }

                        is HomeTasksIntent.DeleteTask -> {
                            repository.deleteTask(intent.taskId)
                        }

                        HomeTasksIntent.FloatingButtonClicked -> {
                            action(HomeTasksAction.OpenTaskBottomSheet())
                        }

                        is HomeTasksIntent.ErrorOccurred -> {
                            updateState { OTrackerState.Error(intent.errorModel) }
                        }

                        is HomeTasksIntent.ClearUserCache -> {
                            repository.clearTasks()
                        }

                        is HomeTasksIntent.TaskChecked -> {
                            try {
                                if (!intent.isCompleted) {
                                    repository.markAsUncompleted(intent.taskId)
                                } else {
                                    repository.markAsCompleted(intent.taskId)
                                }
                            } catch (ex: Exception) {
                                action(HomeTasksAction.ShowSnackbar(ex.message))
                            }
                        }
                    }
                }
            }
        }
}