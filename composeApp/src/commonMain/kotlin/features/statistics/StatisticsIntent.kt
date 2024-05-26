package features.statistics

import pro.respawn.flowmvi.api.MVIIntent

sealed interface StatisticsIntent : MVIIntent{
    data object onTryAgainClicked : StatisticsIntent
}