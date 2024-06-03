package features.tasks.home.mvi

import features.OTrackerState
import flow_mvi.ConfigurationFactory
import flow_mvi.configure
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    private val configurationFactory: ConfigurationFactory,
    private val userStore: UserStore,
) : Container<OTrackerState<Flow<ImmutableList<TaskModel>>>, HomeTasksIntent, HomeTasksAction> {
    override val store: Store<OTrackerState<Flow<ImmutableList<TaskModel>>>, HomeTasksIntent, HomeTasksAction> =
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
                        is HomeTasksIntent.LoadTasks -> {
                            updateState { OTrackerState.Loading }
                            launch { repository.updateTasks(TaskType.ALL, userId) }
                            runCatching {
                                updateState {
                                    OTrackerState.Success(
                                        repository.getCachedTasks().map { it.toPersistentList() })
                                }
                            }.onFailure {
                                action(HomeTasksAction.ShowSnackbar(it.message))
                            }
                        }

                        is HomeTasksIntent.EditTask -> {
                            action(HomeTasksAction.OpenTaskBottomSheet(intent.taskId))
                        }

                        is HomeTasksIntent.DeleteTask -> {
                            runCatching {
                                repository.deleteTask(intent.taskId)?.let {
                                    action(HomeTasksAction.ShowSnackbar("Task \"${it.name}\" was deleted"))
                                }
                            }.onFailure {
                                action(HomeTasksAction.ShowSnackbar(it.message))
                            }
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
                            runCatching {
                                if (!intent.isChecked) {
                                    repository.markAsUncompleted(intent.taskId)
                                } else {
                                    repository.markAsCompleted(intent.taskId)
                                }
                            }.onFailure {
                                action(HomeTasksAction.ShowSnackbar(it.message))
                            }
                        }
                    }
                }
            }
        }
}