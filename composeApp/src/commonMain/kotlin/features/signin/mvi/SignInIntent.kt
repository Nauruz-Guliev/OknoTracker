package features.signin.mvi

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIIntent

@Immutable
sealed interface SignInIntent : MVIIntent {

    sealed interface Internal : SignInIntent

    sealed interface Outer : SignInIntent {

        data class Login(
            val email: String,
            val password: String
        ) : Outer

        data object SignUp : Outer

        data object TryAgain : Outer
    }


}