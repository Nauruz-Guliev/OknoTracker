package ru.kpfu.itis.features.task.data.mapper

import ru.kpfu.itis.common.mapper.Mapper
import ru.kpfu.itis.features.task.data.dto.TaskChangeRequest
import ru.kpfu.itis.features.task.data.dto.TaskCreateRequest
import ru.kpfu.itis.features.task.data.dto.TaskDto
import ru.kpfu.itis.features.task.data.dto.TaskPriorityDto
import ru.kpfu.itis.features.task.domain.model.TaskModel

class TaskMapper : Mapper<TaskDto, TaskModel> {

    override fun mapItem(input: TaskDto): TaskModel {
        return with(input) {
            TaskModel(
                id = id,
                name = name,
                userId = userId,
                isCompleted = isCompleted,
                completedOnTime = completedOnTime,
                description = description,
                lastModifiedTime = lastModifiedTime,
                deadlineTime = deadlineTime,
                completedTime = completedTime,
                priority = priority.name
            )
        }
    }

    fun mapCreate(task: TaskModel): TaskCreateRequest {
        return with(task) {
            TaskCreateRequest(
                name = name,
                description = description,
                userId = userId,
                deadlineTime = deadlineTime,
                priority = TaskPriorityDto.valueOf(priority.uppercase())
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
                priority = TaskPriorityDto.valueOf(priority.uppercase())
            )
        }
    }
}