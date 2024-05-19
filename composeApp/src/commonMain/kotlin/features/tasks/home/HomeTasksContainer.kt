package features.tasks.home

import features.OTrackerState
import flow_mvi.DefaultConfigurationFactory
import flow_mvi.configure
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import ru.kpfu.itis.common.mapper.ErrorMapper
import ru.kpfu.itis.features.task.data.repository.TaskRepository
import ru.kpfu.itis.features.task.data.store.UserStore
import ru.kpfu.itis.features.task.domain.model.TaskModel

class HomeTasksContainer(
    private val errorMapper: ErrorMapper,
    private val repository: TaskRepository,
    private val configurationFactory: DefaultConfigurationFactory,
    private val userStore: UserStore,
) : Container<OTrackerState<List<TaskModel>>, HomeTasksIntent, HomeTasksAction> {

    override val store: Store<OTrackerState<List<TaskModel>>, HomeTasksIntent, HomeTasksAction> =
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
                        is HomeTasksIntent.LoadActiveTasks -> {
                            updateState { OTrackerState.Loading }
                            val tasks = repository.getActiveTasks(userId)
                            updateState { OTrackerState.Success(tasks) }
                        }

                        is HomeTasksIntent.EditTask -> {
                            action(HomeTasksAction.OpenTaskBottomSheet(intent.taskId))
                        }

                        is HomeTasksIntent.DeleteTask -> {
                            updateState { OTrackerState.Loading }
                            repository.deleteTask(intent.taskId)
                            val tasks = repository.getActiveTasks(userId)
                            updateState { OTrackerState.Success(tasks) }
                        }

                        is HomeTasksIntent.LoadAllTasks -> {
                            updateState { OTrackerState.Loading }
                            val tasks = repository.getActiveTasks(userId)
                            updateState { OTrackerState.Success(tasks) }
                        }

                        HomeTasksIntent.FloatingButtonClicked -> {
                            action(HomeTasksAction.OpenTaskBottomSheet())
                        }

                        is HomeTasksIntent.LoadCachedTasks -> {
                            updateState { OTrackerState.Loading }
                            val tasks = repository.getActiveCachedTasks()
                            updateState { OTrackerState.Success(tasks) }
                        }

                        is HomeTasksIntent.ErrorOccurred -> {
                            updateState { OTrackerState.Error(intent.errorModel) }
                        }

                        is HomeTasksIntent.ClearUserCache -> {
                            repository.clearTasks()
                        }
                        is HomeTasksIntent.TaskChecked -> {
                            try {
                                if (intent.isCompleted) {
                                    repository.markAsUncompleted(intent.taskId)
                                } else {
                                    repository.markAsCompleted(intent.taskId)
                                }
                                intent(HomeTasksIntent.LoadAllTasks)
                            } catch (ex: Exception) {
                                action(HomeTasksAction.ShowSnackbar(ex.message))
                            }
                        }
                    }
                }
            }
        }
}