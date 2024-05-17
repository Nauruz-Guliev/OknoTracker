package features.signup

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIIntent

@Immutable
sealed interface SignUpIntent : MVIIntent {

    data class SignUpClicked(
        val email: String,
        val password: String
    ) : SignUpIntent

    data object SignInClicked : SignUpIntent
}