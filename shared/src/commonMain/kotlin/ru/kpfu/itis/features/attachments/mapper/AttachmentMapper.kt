package ru.kpfu.itis.features.attachments.mapper

import ru.kpfu.itis.common.mapper.Mapper
import ru.kpfu.itis.features.attachments.dto.AttachmentDto
import ru.kpfu.itis.features.attachments.dto.AttachmentRequest
import ru.kpfu.itis.features.task.domain.model.AttachmentModel

class AttachmentMapper : Mapper<AttachmentDto, AttachmentModel> {

    override fun mapItem(input: AttachmentDto): AttachmentModel {
        return with(input) {
            AttachmentModel(
                id = id,
                name = name,
                contentType = contentType,
                taskId = taskId,
                content = content.orEmpty()
            )
        }
    }

    fun map(input: AttachmentModel): AttachmentRequest {
        return with(input) {
            AttachmentRequest(
                name = name,
                contentType = contentType,
                taskId = taskId,
                content = content
            )
        }
    }

    fun mapItem(input: AttachmentModel): AttachmentDto {
        return with(input) {
            AttachmentDto(
                id = id,
                name = name,
                contentType = contentType,
                taskId = taskId,
                content = content
            )
        }
    }
}