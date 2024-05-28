package ru.kpfu.itis.features.task.data.service

import dev.icerock.moko.resources.StringResource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.parameters
import ru.kpfu.itis.extensions.getString
import ru.kpfu.itis.features.task.data.dto.TaskChangeRequest
import ru.kpfu.itis.features.task.data.dto.TaskCreateRequest
import ru.kpfu.itis.features.task.data.dto.TaskResponseList
import ru.kpfu.itis.features.task.data.dto.TaskResponseSingle
import ru.kpfu.itis.features.task.data.dto.TaskStatisticDto
import ru.kpfu.itis.features.task.data.dto.TaskStatisticsResponse
import ru.kpfu.itis.shared.MR
import ru.kpfu.itis.utils.Strings

class TaskService(
    private val httpClient: HttpClient,
    private val strings: Strings
) {

    suspend fun getTask(taskId: Long): TaskResponseSingle {
        return httpClient.get(
            "${(MR.strings.url.get())}task/$taskId"
        ).body()
    }

    suspend fun createTask(task: TaskCreateRequest): TaskResponseSingle {
        return httpClient.post {
            url("${(MR.strings.url.get())}task")
            header("Content-Type", "application/json")
            setBody(task)
        }.body()
    }

    suspend fun changeTask(task: TaskChangeRequest): TaskResponseSingle {
        return httpClient.put {
            header("Content-Type", "application/json")
            setBody(task)
            url("${(MR.strings.url.get())}task")
        }.body()
    }

    suspend fun deleteTask(taskId: Long): TaskResponseSingle {
        return httpClient.delete {
            url("${(MR.strings.url.get())}task/$taskId")
            header("Content-Type", "application/json")
        }.body()
    }

    suspend fun markTaskCompleted(taskId: Long): TaskResponseSingle {
        return httpClient.put {
            url("${(MR.strings.url.get())}task/mark_as_completed/$taskId")
        }.body()
    }

    suspend fun markTaskUncompleted(taskId: Long): TaskResponseSingle {
        return httpClient.put {
            url("${(MR.strings.url.get())}task/mark_as_uncompleted/$taskId")
        }.body()
    }

    suspend fun getActiveTasks(
        userId: Long,
        pageSize: Long = 20,
        page: Long = 1,
    ): TaskResponseList {
        println(userId)
        return httpClient.get {
            parameter("page", "$page")
            parameter("pageSize", "$pageSize")
            url("${(MR.strings.url.get())}task/uncompleted_list/order_by_deadline/$userId")
        }.body()
    }

    suspend fun getStatistics(
        userId: Long,
    ): TaskStatisticsResponse {
        // TODO()
//        return TaskStatisticsResponse(
//            data = listOf(
//                TaskStatisticDto(
//                    date = "24 jun",
//                    countOfTasks = 2842,
//                    countOfNotCompletedTasks = 7576,
//                    countOfCompletedTasks = 8482,
//                    countOfOverdueTasks = 4830,
//                    countOfCompletedOnTimeTasks = 4465
//                ),
//                TaskStatisticDto(
//                    date = "25 jun",
//                    countOfTasks = 8886,
//                    countOfNotCompletedTasks = 5866,
//                    countOfCompletedTasks = 5145,
//                    countOfOverdueTasks = 7067,
//                    countOfCompletedOnTimeTasks = 7371
//                ),
//                TaskStatisticDto(
//                    date = "26 jun",
//                    countOfTasks = 5643,
//                    countOfNotCompletedTasks = 5741,
//                    countOfCompletedTasks = 4789,
//                    countOfOverdueTasks = 3242,
//                    countOfCompletedOnTimeTasks = 8870),
//                TaskStatisticDto(
//                    date = "27 jun",
//                    countOfTasks = 24,
//                    countOfNotCompletedTasks = 9644,
//                    countOfCompletedTasks = 86,
//                    countOfOverdueTasks = 7174,
//                    countOfCompletedOnTimeTasks = 9942
//                ),
//                TaskStatisticDto(
//                    date = "28 jun",
//                    countOfTasks = 334338,
//                    countOfNotCompletedTasks = 8402,
//                    countOfCompletedTasks = 825,
//                    countOfOverdueTasks = 5490,
//                    countOfCompletedOnTimeTasks = 6904
//                ),
//                TaskStatisticDto(
//                    date = "29 jun",
//                    countOfTasks = 12064,
//                    countOfNotCompletedTasks = 5944,
//                    countOfCompletedTasks = 85,
//                    countOfOverdueTasks = 6004,
//                    countOfCompletedOnTimeTasks = 3287
//                ),
//            ), isSuccess = false, error = null
//        )
        return httpClient.get {
            url("${(MR.strings.url.get())}task/statistics")
            parameters {
                append("user-id", "$userId")
            }
        }.body()
    }

    suspend fun getCompletedTasks(
        userId: Long,
        page: Long = 1,
        pageSize: Long = 20,
    ): TaskResponseList {
        return httpClient.get {
            parameter("page", "$page")
            parameter("pageSize", "$pageSize")
            url("${MR.strings.url.get()}task/completed_list/order_by_completed_desc/$userId")
        }.body()
    }

    suspend fun getAllTasks(
        userId: Long,
        page: Long = 1,
        pageSize: Long = 20,
    ): TaskResponseList {
        return httpClient.get {
            parameter("page", "$page")
            parameter("pageSize", "$pageSize")
            url("${MR.strings.url.get()}task/all/$userId")
        }.body()
    }

    fun StringResource.get(): String {
        return strings.getString(this)
    }
}