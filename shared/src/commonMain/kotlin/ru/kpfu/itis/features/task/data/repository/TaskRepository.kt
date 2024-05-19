package ru.kpfu.itis.features.task.data.repository

import ru.kpfu.itis.features.task.data.db.TaskDatabaseImpl
import ru.kpfu.itis.features.task.data.mapper.TaskMapper
import ru.kpfu.itis.features.task.data.model.TaskResponseSingle
import ru.kpfu.itis.features.task.data.service.TaskService
import ru.kpfu.itis.features.task.domain.model.TaskModel

class TaskRepository(
    private val taskDatabase: TaskDatabaseImpl,
    private val taskService: TaskService,
    private val taskMapper: TaskMapper,
) {

    suspend fun getTask(taskId: Long): TaskModel {
        return handleTask(taskService.getTask(taskId))
    }

    suspend fun createTask(taskModel: TaskModel) {
        handleTask(taskService.createTask(taskMapper.mapCreate(taskModel)))
    }

    suspend fun changeTask(taskModel: TaskModel) {
        handleTask(taskService.changeTask(taskMapper.mapChange(taskModel)))
    }

    suspend fun deleteTask(taskId: Long) {
        val result = taskService.deleteTask(taskId)
        if (result.error != null) {
            throw taskMapper.mapToException(result.error)
        } else {
            taskDatabase.deleteTask(taskId)
        }
    }

    suspend fun markAsCompleted(taskId: Long) {
        handleTask(taskService.markTaskCompleted(taskId))
    }

    suspend fun markAsUncompleted(taskId: Long) {
        handleTask(taskService.markTaskUncompleted(taskId))
    }

    suspend fun getActiveTasks(userId: Long): List<TaskModel> {
        val response = taskService.getActiveTasks(userId)
        if (response.data?.taskList != null) {
            taskDatabase.clearAndCreateActiveTasks(response.data.taskList)
            return getCachedTasks(userId)
        } else {
            throw taskMapper.mapToException(response.error)
        }
    }

    suspend fun getCachedTasks(userId: Long): List<TaskModel> {
        return taskMapper.map(taskDatabase.getActiveTasks())
    }

    suspend fun clearTasks() {
        taskDatabase.deleteAllTasks()
    }

    private fun handleTask(task: TaskResponseSingle): TaskModel {
        return if (task.data != null) {
            taskDatabase.saveTask(task.data)
            taskMapper.map(task.data)
        } else {
            throw taskMapper.mapToException(task.error)
        }
    }
}