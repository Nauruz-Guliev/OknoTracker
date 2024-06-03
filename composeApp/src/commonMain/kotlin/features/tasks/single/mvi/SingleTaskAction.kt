package features.tasks.single.mvi

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface SingleTaskAction : MVIAction {

    data class ShowSnackbar(val message: String) : SingleTaskAction
    data object CloseBottomSheet : SingleTaskAction
}

@Immutable
data class ImageModel(
    val data: ByteArray,
    val id: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageModel

        if (!data.contentEquals(other.data)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}
