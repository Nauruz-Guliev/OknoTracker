package features.signup

import androidx.compose.runtime.Immutable
import dev.icerock.moko.resources.StringResource
import features.fileds.InputField
import pro.respawn.flowmvi.api.MVIState
import ru.kpfu.itis.common.model.ErrorModel

@Immutable
sealed interface SignUpState : MVIState {
    fun findFieldError(field: InputField): StringResource? =
        (this as? ValidationError)?.fieldErrors?.find { it == field }?.errorText

    data object Initial : SignUpState
    data object Loading : SignUpState
    data class ValidationError(val fieldErrors: List<InputField>) : SignUpState
    data class InternalError(val error: ErrorModel) : SignUpState
    data class NetworkError(val email: String, val password: String) : SignUpState
}