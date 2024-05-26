package features.tasks.single

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

class SingleTaskContainer(
    private val errorMapper: ErrorMapper,
    private val repository: TaskRepository,
    private val configurationFactory: DefaultConfigurationFactory,
    private val userStore: UserStore,
) : Container<OTrackerState<TaskModel>, SingleTaskIntent, SingleTaskAction> {

    override val store: Store<OTrackerState<TaskModel>, SingleTaskIntent, SingleTaskAction> =
        store(OTrackerState.Initial) {

            configure(configurationFactory, "SingleTask")

            recover { exception ->
                updateState {
                    OTrackerState.Error(errorMapper.map(exception = exception))
                }
                null
            }

            reduce { intent ->
                val userId = userStore.getUserId()
                if (userId != null) {
                    when (intent) {
                        is SingleTaskIntent.CreateNew -> {
                            runCatching {
                                repository.createTask(
                                    intent.model.copy(
                                        userId = userId
                                    )
                                )
                                action(SingleTaskAction.CloseBottomSheet)
                            }.onFailure {
                                it.message?.let {
                                    action(SingleTaskAction.ShowSnackbar(it))
                                }
                            }
                        }

                        is SingleTaskIntent.Error -> {
                            action(SingleTaskAction.ShowSnackbar(intent.message))
                        }

                        is SingleTaskIntent.Edit -> {
                            runCatching {
                                repository.changeTask(
                                    intent.model.copy(
                                        userId = userId
                                    )
                                )
                                action(SingleTaskAction.CloseBottomSheet)
                            }.onFailure {
                                it.message?.let {
                                    action(SingleTaskAction.ShowSnackbar(it))
                                }
                            }
                        }

                        is SingleTaskIntent.LoadTask -> {
                            runCatching {
                                repository.getTask(intent.taskId).also {
                                    updateState { OTrackerState.Success(it) }
                                }
                            }.onFailure {
                                it.message?.let {
                                    action(SingleTaskAction.ShowSnackbar(it))
                                }
                            }
                        }
                    }
                }
            }
        }
}



