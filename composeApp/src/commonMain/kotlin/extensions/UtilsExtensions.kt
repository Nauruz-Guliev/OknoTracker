package extensions

import ru.kpfu.itis.features.task.domain.model.AttachmentModel
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun List<AttachmentModel>.mapToByteArray(): List<ByteArray> {
    return map { attachmentModel ->
        Base64.decode(attachmentModel.content)
    }
}