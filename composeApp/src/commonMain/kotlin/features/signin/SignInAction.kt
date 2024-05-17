package features.signin

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction
import ru.kpfu.itis.common.model.ErrorModel


@Immutable
sealed interface SignInAction : MVIAction {

    data object OpenRegisterScreen : SignInAction
    data class OpenMainScreen(val userId: Long) : SignInAction
    data class OpenErrorScreen(val errorModel: ErrorModel) : SignInAction
}