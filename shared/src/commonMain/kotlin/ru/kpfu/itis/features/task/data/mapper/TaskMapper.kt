package ru.kpfu.itis.features.task.data.mapper

import ru.kpfu.itis.common.model.BackendException
import ru.kpfu.itis.common.model.ErrorDto
import ru.kpfu.itis.features.task.data.model.TaskChangeRequest
import ru.kpfu.itis.features.task.data.model.TaskCreateRequest
import ru.kpfu.itis.features.task.data.model.TaskDto
import ru.kpfu.itis.features.task.domain.model.TaskModel

class TaskMapper {

    fun map(task: TaskDto): TaskModel {
        return with(task) {
            TaskModel(
                id = id,
                name = name,
                userId = userId,
                isCompleted = isCompleted,
                completedOnTime = completedOnTime,
                description = description,
                lastModifiedTime = lastModifiedTime,
                deadlineTime = deadlineTime,
                completedTime = completedTime
            )
        }
    }

    fun map(tasks: List<TaskDto>): List<TaskModel> {
        return tasks.map(::map)
    }

    fun mapCreate(task: TaskModel): TaskCreateRequest {
        return with(task) {
            TaskCreateRequest(
                name = name,
                description = description,
                userId = userId,
                deadlineTime = deadlineTime
            )
        }
    }

    fun mapChange(task: TaskModel): TaskChangeRequest {
        return with(task) {
            TaskChangeRequest(
                id = id,
                name = name,
                description = description,
                userId = userId,
                deadlineTime = deadlineTime,
            )
        }
    }

    fun mapToException(errorDto: ErrorDto?): BackendException {
        return with(errorDto) {
            BackendException(
                title = this?.title,
                details = this?.details
            )
        }
    }
}