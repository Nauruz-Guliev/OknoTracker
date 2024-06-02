package extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import features.tasks.TaskPriority
import features.tasks.mapToColor
import features.tasks.single.ImageModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDateTime
import ru.kpfu.itis.features.task.domain.model.AttachmentModel
import ru.kpfu.itis.features.task.domain.model.TaskModel
import theme.secondaryContainerLight
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

@Composable
fun TaskModel.createLabels(): ImmutableList<Pair<String, Color>> {
    return buildList {
        with(this@createLabels) {
            add(this.priority to TaskPriority[this.priority].mapToColor())
            this.deadlineTime?.let {
                add(
                    LocalDateTime.parse(it)
                        .convertToString() to secondaryContainerLight
                )
            }
        }
    }.toPersistentList()
}