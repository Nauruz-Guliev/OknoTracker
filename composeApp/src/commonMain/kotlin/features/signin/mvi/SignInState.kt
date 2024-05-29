package features.signin.mvi

import androidx.compose.runtime.Immutable
import dev.icerock.moko.resources.StringResource
import features.fileds.InputField
import pro.respawn.flowmvi.api.MVIState
import ru.kpfu.itis.common.model.ErrorModel

@Immutable
sealed interface SignInState : MVIState {
    fun findFieldError(field: InputField): StringResource? =
        (this as? ValidationError)?.fieldErrors?.find { it == field }?.errorText

    data object Initial : SignInState
    data object Loading : SignInState
    data class ValidationError(val fieldErrors: List<InputField>) : SignInState
    data class InternalError(val error: ErrorModel) : SignInState
    data class NetworkError(val email: String, val password: String) : SignInState
}

