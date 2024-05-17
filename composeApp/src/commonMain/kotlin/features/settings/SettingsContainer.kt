package features.settings

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

class SettingsContainer(
    private val userStore: UserStore,
    private val configurationFactory: ConfigurationFactory,
    private val errorMapper: ErrorMapper,
) : Container<OTrackerState<Nothing>, SettingsIntent, SettingsAction> {

    override val store: Store<OTrackerState<Nothing>, SettingsIntent, SettingsAction> =
        store(OTrackerState.Initial) {

            configure(configurationFactory, "Settings")

            recover { exception ->
                updateState {
                    OTrackerState.Error(errorMapper.map(exception = exception))
                }
                null
            }

            reduce { intent ->
                when (intent) {
                    is SettingsIntent.Logout -> {
                        userStore.deleteUserId()
                    }

                    is SettingsIntent.ToggleDarkMode -> {
                        TODO()
                    }
                }
            }
        }
}
