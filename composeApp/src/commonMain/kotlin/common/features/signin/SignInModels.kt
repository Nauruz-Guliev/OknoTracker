package common.features.signin

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState
import ru.kpfu.itis.common.domain.model.ErrorModel

@Immutable
sealed interface SignInState : MVIState {

    data object Loading : SignInState
    data class Error(val model: ErrorModel) : SignInState
    data object Initial : SignInState
}

@Immutable
sealed interface SignInIntent : MVIIntent {

    data class SignInClicked(
        val email: String,
        val password: String
    ) : SignInIntent

    data object SignUpClicked : SignInIntent
}

@Immutable
sealed interface SignInAction : MVIAction {

    data object OpenRegisterScreen : SignInAction
    data class OpenMainScreen(val userId: Long) : SignInAction
}