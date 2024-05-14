package ru.kpfu.itis.features.task.data.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.parameters
import ru.kpfu.itis.features.task.data.model.TaskChangeRequest
import ru.kpfu.itis.features.task.data.model.TaskCreateRequest
import ru.kpfu.itis.features.task.data.model.TaskResponseList
import ru.kpfu.itis.features.task.data.model.TaskResponseSingle
import ru.kpfu.itis.features.task.data.model.TaskStatisticResponse
import ru.kpfu.itis.shared.MR

class TaskService(
    private val httpClient: HttpClient
) {

    /*
    HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            useAlternativeNames = false
        })
    }
}
     */

    suspend fun getTask(taskId: String): TaskResponseSingle {
        return httpClient.get(
            "${MR.strings.url}/task/$taskId"
        ).body()
    }

    suspend fun createTask(task: TaskCreateRequest): TaskResponseSingle {
        return httpClient.post {
            url("${MR.strings.url}/task/")
            setBody(task)
        }.body()
    }

    suspend fun changeTask(task: TaskChangeRequest): TaskResponseSingle {
        return httpClient.put {
            url("${MR.strings.url}/task/")
            setBody(task)
        }.body()
    }

    suspend fun deleteTask(taskId: Long): TaskResponseSingle {
        return httpClient.delete {
            url("${MR.strings.url}/task/$taskId")
        }.body()
    }

    suspend fun markTaskCompleted(taskId: Long): TaskResponseSingle {
        return httpClient.put {
            url("${MR.strings.url}/task/mark_as_completed/$taskId")
        }.body()
    }

    suspend fun markTaskUncompleted(taskId: Long): TaskResponseSingle {
        return httpClient.put {
            url("${MR.strings.url}/task/mark_as_uncompleted/$taskId")
        }.body()
    }

    suspend fun getActiveTasks(
        userId: Long,
        pageSize: Long = 20,
        page: Long = 0,
    ): List<TaskResponseList> {
        return httpClient.get {
            url("${MR.strings.url}/task/uncompleted_list/order_by_deadline/$userId")
            parameters {
                append("page", "$page")
                append("pageSize", "$pageSize")
            }
        }.body()
    }

    suspend fun getStatistics(
        userId: Long,
        startDay: String,
        endDay: String,
    ): TaskStatisticResponse {
        return httpClient.get {
            url("${MR.strings.url}/task/statistics")
            parameters {
                append("user-id", "$userId")
                append("startDay", startDay)
                append("endDay", endDay)
            }
        }.body()
    }

    suspend fun getCompletedTasks(
        userId: Long,
        pageSize: Long = 20,
        page: Long = 0,
    ): TaskResponseList {
        return httpClient.get {
            url("${MR.strings.url}/task/completed_list/order_by_completed_desc/$userId")
            parameters {
                append("page", "$page")
                append("pageSize", "$pageSize")
            }
        }.body()
    }
}