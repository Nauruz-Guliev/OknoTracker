package features.signup.mvi

import androidx.compose.runtime.Immutable
import features.signin.mvi.SignInAction
import pro.respawn.flowmvi.api.MVIAction
import ru.kpfu.itis.common.model.ErrorModel

@Immutable
sealed interface SignUpAction : MVIAction {

    data object OpenSignInScreen : SignUpAction
    data class OpenMainScreen(val userId: Long) : SignUpAction
    data class ShowSnackbar(val text: String, val actionLabel: String): SignUpAction
}