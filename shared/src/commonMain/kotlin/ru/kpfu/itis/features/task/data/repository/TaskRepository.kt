package ru.kpfu.itis.features.task.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.kpfu.itis.features.attachments.repository.AttachmentRepository
import ru.kpfu.itis.features.task.data.db.TaskDbImpl
import ru.kpfu.itis.features.task.data.dto.TaskResponseSingle
import ru.kpfu.itis.features.task.data.mapper.TaskMapper
import ru.kpfu.itis.features.task.data.model.TaskType
import ru.kpfu.itis.features.task.data.service.TaskService
import ru.kpfu.itis.features.task.domain.model.TaskModel

class TaskRepository(
    private val taskDatabase: TaskDbImpl,
    private val taskService: TaskService,
    private val taskMapper: TaskMapper,
    private val dispatcher: CoroutineDispatcher,
    private val attachmentRepository: AttachmentRepository,
) {

    suspend fun getTask(taskId: Long): TaskModel = withContext(dispatcher) {
        handleTask(taskService.getTask(taskId))
    }

    suspend fun createTask(taskModel: TaskModel): TaskModel = withContext(dispatcher) {
        handleTask(taskService.createTask(taskMapper.mapCreate(taskModel)))
    }

    suspend fun changeTask(taskModel: TaskModel): TaskModel = withContext(dispatcher) {
        handleTask(taskService.changeTask(taskMapper.mapChange(taskModel)))
    }

    suspend fun deleteTask(taskId: Long) = withContext(dispatcher) {
        val result = taskService.deleteTask(taskId)
        if (result.error != null) {
            throw taskMapper.mapToException(result.error)
        } else {
            taskDatabase.deleteTask(taskId)
        }
        result.data?.let { taskMapper.mapItem(it) }
    }

    suspend fun markAsCompleted(taskId: Long): TaskModel = withContext(dispatcher) {
        handleTask(taskService.markTaskCompleted(taskId))
    }

    suspend fun markAsUncompleted(taskId: Long): TaskModel = withContext(dispatcher) {
        handleTask(taskService.markTaskUncompleted(taskId))
    }

    suspend fun updateTasks(taskType: TaskType, userId: Long) {
        val response = when (taskType) {
            TaskType.ALL -> taskService.getAllTasks(userId)
            TaskType.ACTIVE -> taskService.getActiveTasks(userId)
            TaskType.CLOSED -> taskService.getCompletedTasks(userId)
        }
        if (response.data?.taskList != null) {
            when (taskType) {
                TaskType.ALL -> taskDatabase.clearAndCreateAllTasks(response.data.taskList)
                TaskType.ACTIVE -> taskDatabase.clearAndCreateActiveTasks(response.data.taskList)
                TaskType.CLOSED -> taskDatabase.clearAndCreateCompletedTasks(response.data.taskList)
            }
        } else {
            throw taskMapper.mapToException(response.error)
        }
    }

    fun getCachedTasks(): Flow<List<TaskModel>> {
        return taskDatabase.getAllTasks().map(taskMapper::mapList).addAttachments()
    }

    fun getCachedCompletedTasks(): Flow<List<TaskModel>> {
        return taskDatabase.getCompletedTasks().map(taskMapper::mapList).addAttachments()
    }

    suspend fun clearTasks() = withContext(dispatcher) {
        taskDatabase.deleteAllTasks()
    }

    private suspend fun handleTask(task: TaskResponseSingle): TaskModel {
        return if (task.data != null) {
            taskDatabase.saveTask(task.data)
            taskMapper.mapItem(task.data)
        } else {
            throw taskMapper.mapToException(task.error)
        }.addAttachment()
    }

    private suspend fun TaskModel.addAttachment(): TaskModel {
        return this.apply {
            attachments.forEach {
                attachmentRepository.saveAttachment(it)
            }
        }
    }

    private fun Flow<List<TaskModel>>.addAttachments(): Flow<List<TaskModel>> {
        return map { taskList ->
            taskList.map { task ->
                task.copy(
                    attachments = attachmentRepository.getCachedAttachments(task.id)
                )
            }
        }
    }
}