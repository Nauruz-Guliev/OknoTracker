package features.statistics

import features.OTrackerState
import flow_mvi.ConfigurationFactory
import flow_mvi.configure
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import ru.kpfu.itis.common.mapper.ErrorMapper
import ru.kpfu.itis.features.task.data.store.UserStore

class StatisticsContainer(
    private val userStore: UserStore,
    private val configurationFactory: ConfigurationFactory,
    private val errorMapper: ErrorMapper,
) : Container<OTrackerState<StatisticsState>, StatisticsIntent, StatisticsAction> {

    override val store: Store<OTrackerState<StatisticsState>, StatisticsIntent, StatisticsAction> =
        store(OTrackerState.Initial) {

            configure(configurationFactory, "Statistics")

            recover { exception ->
                updateState { OTrackerState.Error(error = errorMapper.map(exception = exception)) }
                null
            }

            reduce { intent ->
                when(intent){
                    StatisticsIntent.onTryAgainClicked -> TODO()
                }
            }
        }
}