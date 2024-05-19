package features.signin

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface SignInAction : MVIAction {

    data object OpenSignUpScreen : SignInAction
    data class OpenMainScreen(val userId: Long) : SignInAction
}