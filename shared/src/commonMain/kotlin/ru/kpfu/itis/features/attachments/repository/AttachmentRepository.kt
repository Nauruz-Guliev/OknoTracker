package ru.kpfu.itis.features.attachments.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.features.attachments.dto.AttachmentResponse
import ru.kpfu.itis.features.attachments.mapper.AttachmentMapper
import ru.kpfu.itis.features.task.data.db.AttachmentDbImpl
import ru.kpfu.itis.features.task.data.service.AttachmentService
import ru.kpfu.itis.features.task.domain.model.AttachmentModel

class AttachmentRepository(
    private val dispatcher: CoroutineDispatcher,
    private val service: AttachmentService,
    private val mapper: AttachmentMapper,
    private val attachmentDbImpl: AttachmentDbImpl,
) {

    suspend fun getCachedAttachments(taskId: Long): List<AttachmentModel> =
        withContext(dispatcher) {
            attachmentDbImpl.getAllAttachments(taskId).map(mapper::mapItem)
        }

    suspend fun getAttachment(id: Long): AttachmentModel = withContext(dispatcher) {
        handleResponse(service.getAttachment(id))
    }

    suspend fun saveAttachment(attachmentModel: AttachmentModel): AttachmentModel =
        withContext(dispatcher) {
            handleResponse(service.saveAttachment(mapper.map(attachmentModel)))
        }

    private fun handleResponse(response: AttachmentResponse): AttachmentModel {
        return if (response.data != null) {
            attachmentDbImpl.saveAttachment(response.data)
            mapper.mapItem(response.data)
        } else {
            throw mapper.mapToException(response.error)
        }
    }
}