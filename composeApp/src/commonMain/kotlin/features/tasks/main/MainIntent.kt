package features.tasks.main

import features.tasks.home.HomeTasksIntent
import pro.respawn.flowmvi.api.MVIIntent
import ru.kpfu.itis.common.model.ErrorModel

sealed interface MainIntent : MVIIntent {

    data class ErrorOccurred(val errorModel: ErrorModel) : MainIntent
    data object FloatingButtonClicked : MainIntent
}