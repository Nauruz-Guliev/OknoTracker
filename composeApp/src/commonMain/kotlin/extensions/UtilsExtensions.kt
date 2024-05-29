package extensions

import features.tasks.single.ImageModel
import ru.kpfu.itis.features.task.domain.model.AttachmentModel
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun List<AttachmentModel>.mapToByteArray(): List<ImageModel> {
    return map { attachmentModel ->
        ImageModel(
            Base64.decode(attachmentModel.content),
            attachmentModel.id
        )
    }
}