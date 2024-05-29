package features.statistics.mvi

import features.OTrackerState
import features.statistics.UiStatisticsState
import features.statistics.mapper.mapStatistics
import flow_mvi.ConfigurationFactory
import flow_mvi.configure
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.init
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import ru.kpfu.itis.common.mapper.ErrorMapper
import ru.kpfu.itis.features.statistics.StatisticsRepository

class StatisticsContainer(
    private val configurationFactory: ConfigurationFactory,
    private val errorMapper: ErrorMapper,
    private val repository: StatisticsRepository,
) : Container<OTrackerState<UiStatisticsState>, StatisticsIntent, StatisticsAction> {

    override val store: Store<OTrackerState<UiStatisticsState>, StatisticsIntent, StatisticsAction> =
        store(initial = OTrackerState.Initial) {

            configure(configurationFactory, "Statistics")

            init {
                loadStatistics()
            }

            recover { exception ->
                updateState { OTrackerState.Error(error = errorMapper.map(exception = exception)) }
                null
            }

            reduce { intent ->
                when (intent) {
                    StatisticsIntent.Outer.TryAgain -> loadStatistics()
                }
            }
        }

    private suspend fun PipelineContext<OTrackerState<UiStatisticsState>, StatisticsIntent, StatisticsAction>.loadStatistics() {
        updateState { OTrackerState.Loading }
        runCatching { repository.getStatistics() }.fold(
            onSuccess = { statisticsModel ->
                updateState { OTrackerState.Success(statisticsModel.mapStatistics()) }
            },
            onFailure = {
                updateState { OTrackerState.Error(error = errorMapper.map(exception = it)) }
            }
        )
    }
}