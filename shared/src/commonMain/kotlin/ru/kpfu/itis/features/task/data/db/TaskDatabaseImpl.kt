package ru.kpfu.itis.features.task.data.db

import ru.kpfu.itis.common.data.driver.DatabaseDriverFactory
import ru.kpfu.itis.features.task.TaskDatabase
import ru.kpfu.itis.features.task.data.model.TaskDto

internal class TaskDatabaseImpl(
    databaseDriverFactory: DatabaseDriverFactory
) {

    private val database = TaskDatabase(
        driver = databaseDriverFactory.createDriver(),
    )

    private val dbQuery = database.taskDatabaseQueries

    internal fun getAllTasks(): List<TaskDto> {
        return dbQuery.selectAllTasks(::mapTask).executeAsList()
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
        completedOnTime: Boolean,
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

    internal fun clearAndCreateTasks(tasks: List<TaskDto>) {
        dbQuery.transaction {
            dbQuery.removeAllTasks()
            tasks.forEach { task ->
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
        }
    }
}