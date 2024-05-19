package features.signin

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction
import ru.kpfu.itis.common.model.ErrorModel


@Immutable
sealed interface SignInAction : MVIAction {

    data object OpenSignUpScreen : SignInAction
    data class OpenMainScreen(val userId: Long) : SignInAction
}