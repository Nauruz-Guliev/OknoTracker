package features.tasks.completed

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
import ru.kpfu.itis.features.task.domain.model.TaskModel

class CompletedTasksContainer(
    private val errorMapper: ErrorMapper,
    private val repository: TaskRepository,
    private val configurationFactory: DefaultConfigurationFactory,
) : Container<TasksState<List<TaskModel>>, CompletedTasksIntent, CompletedTasksAction> {

    override val store: Store<TasksState<List<TaskModel>>, CompletedTasksIntent, CompletedTasksAction> =
        store(TasksState.Initial) {

            configure(configurationFactory, "Tasks")

            recover { exception ->
                updateState {
                    TasksState.Error(errorMapper.map(exception = exception))
                }
                null
            }

            reduce { intent ->
                when (intent) {
                    is CompletedTasksIntent.LoadTasks -> {
                        updateState { TasksState.Loading }
                        val tasks = loadActiveTasks(2)
                        updateState { TasksState.Success(tasks) }
                    }

                    is CompletedTasksIntent.EditTask -> {
                        val task = repository.getTask(intent.taskId)
                        action(CompletedTasksAction.OpenTaskBottomSheet(task.id))
                    }

                    is CompletedTasksIntent.DeleteTask -> {
                        updateState { TasksState.Loading }
                        repository.deleteTask(intent.taskId)
                        val tasks = loadActiveTasks(2)
                        updateState { TasksState.Success(tasks) }
                    }
                }
            }
        }

    private suspend fun loadActiveTasks(userId: Long): List<TaskModel> {
        return repository.getActiveTasks(userId)
    }
}