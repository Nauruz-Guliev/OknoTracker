package features.signup.mvi

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIIntent

@Immutable
sealed interface SignUpIntent : MVIIntent {

    data class SignUp(
        val email: String,
        val password: String
    ) : SignUpIntent

    data object SignIn : SignUpIntent

    data object TryAgain : SignUpIntent
}