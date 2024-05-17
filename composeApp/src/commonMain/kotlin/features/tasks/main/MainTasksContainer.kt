package features.tasks.main

import features.tasks.TasksState
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

class MainTasksContainer(
    private val errorMapper: ErrorMapper,
    private val repository: TaskRepository,
    private val configurationFactory: DefaultConfigurationFactory,
    private val userStore: UserStore,
) : Container<TasksState<List<TaskModel>>, MainTasksIntent, MainTasksAction> {

    override val store: Store<TasksState<List<TaskModel>>, MainTasksIntent, MainTasksAction> =
        store(TasksState.Initial) {

            configure(configurationFactory, "Tasks")

            recover { exception ->
                updateState { TasksState.Error(errorMapper.map(exception = exception)) }
                null
            }

            reduce { intent ->
                val userId = userStore.getUserId()
                if (userId == null) {
                    action(MainTasksAction.SignOut)
                    intent(MainTasksIntent.ClearCache)
                } else {
                    when (intent) {
                        is MainTasksIntent.LoadActiveTasks -> {
                            updateState { TasksState.Loading }
                            val tasks = repository.getActiveTasks(userId)
                            updateState { TasksState.Success(tasks) }
                        }

                        is MainTasksIntent.EditTask -> {
                            action(MainTasksAction.OpenTaskBottomSheet(intent.taskId))
                        }

                        is MainTasksIntent.DeleteTask -> {
                            updateState { TasksState.Loading }
                            repository.deleteTask(intent.taskId)
                            val tasks = repository.getActiveTasks(userId)
                            updateState { TasksState.Success(tasks) }
                        }

                        is MainTasksIntent.LoadAllTasks -> {
                            updateState { TasksState.Loading }
                            val tasks = repository.getActiveTasks(userId)
                            updateState { TasksState.Success(tasks) }
                        }

                        MainTasksIntent.FloatingButtonClicked -> {
                            action(MainTasksAction.OpenTaskBottomSheet())
                        }

                        is MainTasksIntent.ErrorOccurred -> {
                            action(MainTasksAction.OpenErrorScreen(intent.errorModel, userId))
                        }

                        is MainTasksIntent.ClearCache -> {
                            repository.clearTasks()
                        }
                    }
                }
            }
        }
}