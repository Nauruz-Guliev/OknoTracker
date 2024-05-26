package features.statistics

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction

@Immutable
sealed interface StatisticsAction : MVIAction{
    data object LoadStatistics : StatisticsAction
}