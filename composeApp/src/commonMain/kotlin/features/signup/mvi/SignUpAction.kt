package features.signup.mvi

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface SignUpAction : MVIAction {

    data object OpenSignInScreen : SignUpAction
    data class OpenMainScreen(val userId: Long) : SignUpAction
    data class ShowSnackbar(val text: String, val actionLabel: String) : SignUpAction
}