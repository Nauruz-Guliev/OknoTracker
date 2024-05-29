package features.signin.mvi

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface SignInAction : MVIAction {

    data object OpenSignUpScreen : SignInAction
    data class OpenMainScreen(val userId: Long) : SignInAction
    data class ShowSnackbar(val text: String, val actionLabel: String) : SignInAction
}