package features.tasks.single

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface SingleTaskAction : MVIAction {

    data class ShowSnackbar(val message: String) : SingleTaskAction
    data object CloseBottomSheet : SingleTaskAction
    data class AddSelectedImage(val image: ImageModel) : SingleTaskAction
    data class DeleteAttachment(val id: Long) : SingleTaskAction
}

data class ImageModel(
    val data: ByteArray,
    val id: Long
)
