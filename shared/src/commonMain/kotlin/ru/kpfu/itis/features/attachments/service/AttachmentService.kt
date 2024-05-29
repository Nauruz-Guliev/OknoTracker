package ru.kpfu.itis.features.attachments.service

import dev.icerock.moko.resources.StringResource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import ru.kpfu.itis.extensions.getString
import ru.kpfu.itis.features.attachments.dto.AttachmentMultipleResponse
import ru.kpfu.itis.features.attachments.dto.AttachmentRequest
import ru.kpfu.itis.features.attachments.dto.AttachmentSingleResponse
import ru.kpfu.itis.shared.MR
import ru.kpfu.itis.utils.Strings

class AttachmentService(
    private val httpClient: HttpClient,
    private val strings: Strings
) {

    suspend fun getAttachment(id: Long): AttachmentSingleResponse {
        return httpClient.get(
            "${(MR.strings.url())}attachment/$id"
        ).body()
    }

    suspend fun deleteAttachment(id: Long): AttachmentSingleResponse {
        return httpClient.delete(
            "${(MR.strings.url())}attachment/$id"
        ).body()
    }

    suspend fun getAllAttachments(
        taskId: Long,
        pageSize: Long = 1000,
        page: Long = 1,
    ): AttachmentMultipleResponse {
        return httpClient.get {
            parameter("page", "$page")
            parameter("pageSize", "$pageSize")
            url("${(MR.strings.url())}attachment/list_by_task/$taskId")
        }.body()
    }

    suspend fun saveAttachment(request: AttachmentRequest): AttachmentSingleResponse {
        return httpClient.post {
            url("${(MR.strings.url())}attachment")
            header("Content-Type", "application/json")
            setBody(request)
        }.body()
    }

    operator fun StringResource.invoke(): String {
        return strings.getString(this)
    }
}