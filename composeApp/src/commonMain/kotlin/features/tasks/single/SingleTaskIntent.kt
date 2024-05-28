package features.tasks.single

import androidx.compose.runtime.Immutable
import io.github.vinceglb.picker.core.PlatformFile
import pro.respawn.flowmvi.api.MVIIntent
import ru.kpfu.itis.features.task.domain.model.TaskModel

@Immutable
sealed interface SingleTaskIntent : MVIIntent {

    data class LoadTask(val taskId: Long) : SingleTaskIntent
    data class CreateNew(val model: TaskModel) : SingleTaskIntent
    data class Edit(val model: TaskModel, val attachments: List<ByteArray>) : SingleTaskIntent
    data class Error(val message: String) : SingleTaskIntent
    data class OnFileSelected(
        val file: PlatformFile?,
        val taskId: Long,
    ) : SingleTaskIntent
}