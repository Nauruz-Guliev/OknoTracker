package features.signin

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIIntent
import ru.kpfu.itis.common.model.ErrorModel

@Immutable
sealed interface SignInIntent : MVIIntent {

    data class SignInClicked(
        val email: String,
        val password: String
    ) : SignInIntent

    data object SignUpClicked : SignInIntent
    data class ErrorOccured(
        val errorModel: ErrorModel
    ) : SignInIntent
}