package ru.kpfu.itis.features.task.data.service

import dev.icerock.moko.resources.StringResource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import ru.kpfu.itis.extensions.getString
import ru.kpfu.itis.features.attachments.dto.AttachmentRequest
import ru.kpfu.itis.features.attachments.dto.AttachmentResponse
import ru.kpfu.itis.shared.MR
import ru.kpfu.itis.utils.Strings

class AttachmentService(
    private val httpClient: HttpClient,
    private val strings: Strings
) {

    suspend fun getAttachment(id: Long): AttachmentResponse {
        return httpClient.get(
            "${(MR.strings.url())}attachment/$id"
        ).body()
    }

    suspend fun saveAttachment(request: AttachmentRequest): AttachmentResponse {
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