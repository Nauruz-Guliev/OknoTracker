package ru.kpfu.itis.features.task.data.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.common.driver.DatabaseDriverFactory
import ru.kpfu.itis.features.task.TaskDatabase
import ru.kpfu.itis.features.task.data.dto.TaskDto

class TaskDatabaseImpl(
    databaseDriverFactory: DatabaseDriverFactory,
    private val dispatcher: CoroutineDispatcher,
) {

    private val database = TaskDatabase(
        driver = databaseDriverFactory.createDriver(),
    )

    private val dbQuery = database.taskDatabaseQueries

    internal fun getAllTasks(): Flow<List<TaskDto>> {
        return dbQuery.getAllTasks(::mapTask).asFlow().mapToList(dispatcher)
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
            try {
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
            } catch (ex: Exception) {
                println(ex)
            }
        }
    }

    internal fun deleteTask(taskId: Long) {
        dbQuery.deleteTaskById(taskId)
    }
}