package ru.kpfu.itis.features.attachments.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.features.attachments.dto.AttachmentSingleResponse
import ru.kpfu.itis.features.attachments.mapper.AttachmentMapper
import ru.kpfu.itis.features.attachments.service.AttachmentService
import ru.kpfu.itis.features.task.data.db.AttachmentDbImpl
import ru.kpfu.itis.features.task.domain.model.AttachmentModel

class AttachmentRepository(
    private val dispatcher: CoroutineDispatcher,
    private val service: AttachmentService,
    private val mapper: AttachmentMapper,
    private val attachmentDbImpl: AttachmentDbImpl,
) {

    suspend fun getCachedAttachments(taskId: Long): List<AttachmentModel> =
        withContext(dispatcher) {
            attachmentDbImpl.getAllAttachments(taskId).map(mapper::mapItem).ifEmpty {
                updateAttachments(taskId)
            }
        }

    suspend fun updateAttachments(taskId: Long): List<AttachmentModel> = withContext(dispatcher) {
        val response = service.getAllAttachments(taskId).data
        response?.data?.map {
            if (it.content.isNullOrBlank() || it.content.isEmpty()) {
                service.getAttachment(it.id).data
            } else {
                it
            } ?: it
        }.also {
            it?.let { attachmentDbImpl.clearAndCreateAttachments(it) }
        }?.map(mapper::mapItem) ?: emptyList()
    }

    suspend fun getAttachment(id: Long): AttachmentModel = withContext(dispatcher) {
        handleResponse(service.getAttachment(id))
    }

    suspend fun saveAttachment(attachmentModel: AttachmentModel): AttachmentModel =
        withContext(dispatcher) {
            handleResponse(service.saveAttachment(mapper.map(attachmentModel)).also {
                it.data?.let { it1 -> attachmentDbImpl.saveAttachment(it1) }
            })
        }

    suspend fun deleteAttachment(id: Long): AttachmentModel =
        withContext(dispatcher) {
            handleResponse(service.deleteAttachment(id)).also {
                attachmentDbImpl.deleteAttachment(id)
            }
        }

    private fun handleResponse(response: AttachmentSingleResponse): AttachmentModel {
        return if (response.data != null) {
            mapper.mapItem(response.data)
        } else {
            throw mapper.mapToException(response.error)
        }
    }
}