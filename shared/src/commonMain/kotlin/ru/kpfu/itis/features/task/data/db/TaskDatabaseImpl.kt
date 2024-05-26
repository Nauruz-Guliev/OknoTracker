package ru.kpfu.itis.features.task.data.db

import ru.kpfu.itis.common.driver.DatabaseDriverFactory
import ru.kpfu.itis.features.task.TaskDatabase
import ru.kpfu.itis.features.task.data.dto.TaskDto

class TaskDatabaseImpl(
    databaseDriverFactory: DatabaseDriverFactory
) {

    private val database = TaskDatabase(
        driver = databaseDriverFactory.createDriver(),
    )

    private val dbQuery = database.taskDatabaseQueries

    internal fun getAllTasks(): List<TaskDto> {
        return dbQuery.getAllTasks(::mapTask).executeAsList()
    }

    private fun mapTask(
        id: Long,
        name: String,
        description: String?,
        userId: Long,
        isCompleted: Boolean,
        lastModifiedTime: String?,
        deadlineTime: String?,
        completedTime: String?,
        completedOnTime: Boolean?,
    ): TaskDto {
        return TaskDto(
            id = id,
            name = name,
            description = description,
            userId = userId,
            isCompleted = isCompleted,
            lastModifiedTime = lastModifiedTime,
            deadlineTime = deadlineTime,
            completedTime = completedTime,
            completedOnTime = completedOnTime
        )
    }

    internal fun clearAndCreateActiveTasks(tasks: List<TaskDto>) {
        dbQuery.transaction {
            dbQuery.deleteActiveTasks()
            tasks.forEach(::saveTask)
        }
    }

    internal fun clearAndCreateCompletedTasks(tasks: List<TaskDto>) {
        dbQuery.transaction {
            dbQuery.deleteCompletedTasks()
            tasks.forEach(::saveTask)
        }
    }

    internal fun clearAndCreateAllTasks(tasks: List<TaskDto>) {
        dbQuery.transaction {
            dbQuery.deleteAllTasks()
            tasks.forEach(::saveTask)
        }
    }

    internal fun deleteAllTasks() {
        dbQuery.transaction {
            dbQuery.deleteAllTasks()
        }
    }

    internal fun saveTask(task: TaskDto) {
        with(task) {
            dbQuery.insertTask(
                id = id,
                name = name,
                description = description,
                userId = userId,
                isCompleted = isCompleted,
                lastModifiedTime = lastModifiedTime,
                deadlineTime = deadlineTime,
                completedTime = completedTime,
                completedOnTime = completedOnTime
            )
        }
    }

    internal fun deleteTask(taskId: Long) {
        dbQuery.deleteTaskById(taskId)
    }

    internal fun getCompletedTasks(): List<TaskDto> {
        return dbQuery.getCompletedTasks(::mapTask).executeAsList()
    }

    internal fun getActiveTasks(): List<TaskDto> {
        return dbQuery.getActiveTasks(::mapTask).executeAsList()
    }
}