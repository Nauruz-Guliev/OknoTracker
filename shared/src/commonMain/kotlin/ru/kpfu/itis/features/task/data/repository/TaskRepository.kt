package ru.kpfu.itis.features.task.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.features.task.data.db.TaskDatabaseImpl
import ru.kpfu.itis.features.task.data.mapper.TaskMapper
import ru.kpfu.itis.features.task.data.dto.TaskResponseSingle
import ru.kpfu.itis.features.task.data.service.TaskService
import ru.kpfu.itis.features.task.domain.model.TaskModel

class TaskRepository(
    private val taskDatabase: TaskDatabaseImpl,
    private val taskService: TaskService,
    private val taskMapper: TaskMapper,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getTask(taskId: Long): TaskModel = withContext(dispatcher) {
        handleTask(taskService.getTask(taskId))
    }

    suspend fun createTask(taskModel: TaskModel) = withContext(dispatcher) {
        handleTask(taskService.createTask(taskMapper.mapCreate(taskModel)))
    }

    suspend fun changeTask(taskModel: TaskModel) = withContext(dispatcher) {
        handleTask(taskService.changeTask(taskMapper.mapChange(taskModel)))
    }

    suspend fun deleteTask(taskId: Long) = withContext(dispatcher) {
        val result = taskService.deleteTask(taskId)
        if (result.error != null) {
            throw taskMapper.mapToException(result.error)
        } else {
            taskDatabase.deleteTask(taskId)
        }
    }

    suspend fun markAsCompleted(taskId: Long) = withContext(dispatcher) {
        handleTask(taskService.markTaskCompleted(taskId))
    }

    suspend fun markAsUncompleted(taskId: Long) = withContext(dispatcher) {
        handleTask(taskService.markTaskUncompleted(taskId))
    }

    suspend fun getActiveTasks(userId: Long): List<TaskModel> = withContext(dispatcher) {
        val response = taskService.getActiveTasks(userId)
        if (response.data?.taskList != null) {
            taskDatabase.clearAndCreateActiveTasks(response.data.taskList)
            getActiveCachedTasks()
        } else {
            throw taskMapper.mapToException(response.error)
        }
    }

    suspend fun getCompleteTasks(userId: Long): List<TaskModel> = withContext(dispatcher) {
        val response = taskService.getCompletedTasks(userId)
        if (response.data?.taskList != null) {
            taskDatabase.clearAndCreateCompletedTasks(response.data.taskList)
            getCompletedCachedTasks()
        } else {
            throw taskMapper.mapToException(response.error)
        }
    }

    suspend fun getActiveCachedTasks(): List<TaskModel> = withContext(dispatcher) {
        taskMapper.mapList(taskDatabase.getActiveTasks())
    }

    suspend fun getCompletedCachedTasks(): List<TaskModel> = withContext(dispatcher) {
        taskMapper.mapList(taskDatabase.getCompletedTasks())
    }

    suspend fun clearTasks() = withContext(dispatcher) {
        taskDatabase.deleteAllTasks()
    }

    private fun handleTask(task: TaskResponseSingle): TaskModel {
        return if (task.data != null) {
            taskDatabase.saveTask(task.data)
            taskMapper.mapItem(task.data)
        } else {
            throw taskMapper.mapToException(task.error)
        }
    }
}