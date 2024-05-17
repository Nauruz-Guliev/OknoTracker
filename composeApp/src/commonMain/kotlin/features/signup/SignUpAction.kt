package features.signup

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction
import ru.kpfu.itis.common.model.ErrorModel

@Immutable
sealed interface SignUpAction : MVIAction {

    data object OpenSignInScreen : SignUpAction
    data class OpenMainScreen(val userId: Long) : SignUpAction
    data class OpenErrorScreen(val errorModel: ErrorModel) : SignUpAction
}