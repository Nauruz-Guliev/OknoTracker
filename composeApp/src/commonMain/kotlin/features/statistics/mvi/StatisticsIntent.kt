package features.statistics.mvi

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIIntent

@Immutable
sealed interface StatisticsIntent : MVIIntent {

    sealed interface Inner

    sealed interface Outer {
        data object TryAgain : StatisticsIntent
    }
}