package ru.kpfu.itis.features.task.data.db

import ru.kpfu.itis.common.driver.DatabaseDriverFactory
import ru.kpfu.itis.features.attachments.dto.AttachmentDto
import ru.kpfu.itis.features.db.TaskDatabase

class AttachmentDbImpl(
    databaseDriverFactory: DatabaseDriverFactory,
) {

    private val database = TaskDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.attachmentDatabaseQueries

    internal fun getAllAttachments(taskId: Long): List<AttachmentDto> {
        return dbQuery.getAllAttachments(taskId, ::mapAttachment).executeAsList()
    }

    private fun mapAttachment(
        id: Long,
        name: String,
        contentType: String,
        taskId: Long,
        content: String,
    ): AttachmentDto {
        return AttachmentDto(
            id = id,
            name = name,
            contentType = contentType,
            content = content,
            taskId = taskId,
        )
    }

    internal fun clearAndCreateAttachments(attachments: List<AttachmentDto>) {
        dbQuery.transaction {
            dbQuery.deleteAllAttachments()
            attachments.forEach(::saveAttachment)
        }
    }

    internal fun deleteAttachment(id: Long) {
        dbQuery.deleteAllAttachments()
    }

    internal fun saveAttachment(attachment: AttachmentDto) {
        with(attachment) {
            dbQuery.insertAttachment(
                id = id,
                name = name,
                contentType = contentType,
                taskId = taskId,
                content = content.orEmpty()
            )
        }
    }
}
