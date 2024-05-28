package features.settings.mvi


import features.OTrackerState
import features.notifications.REMINDER_NOTIFICATION_ID
import features.settings.SettingsState
import features.settings.UiSettingItem
import features.settings.toDomain
import features.settings.toUi
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
import ru.kpfu.itis.features.notifications.CommonNotificationManager
import ru.kpfu.itis.features.notifications.CommonNotificationsScheduler
import ru.kpfu.itis.features.notifications.ScheduleIntervals
import ru.kpfu.itis.features.settings.data.SettingStorage
import ru.kpfu.itis.features.settings.domain.SettingItem
import ru.kpfu.itis.features.settings.domain.SettingKey
import ru.kpfu.itis.features.task.data.store.UserStore

class SettingsContainer(
    private val userStore: UserStore,
    private val configurationFactory: ConfigurationFactory,
    private val errorMapper: ErrorMapper,
    private val settingsStorage: SettingStorage,
    private val commonNotificationManager: CommonNotificationManager,
    private val elapsedRealtimeProvider: () -> Long,
    private val commonNotificationsScheduler: CommonNotificationsScheduler
) : Container<OTrackerState<SettingsState>, SettingsIntent, SettingsAction> {

    override val store: Store<OTrackerState<SettingsState>, SettingsIntent, SettingsAction> =
        store(OTrackerState.Initial) {

            configure(configurationFactory, "Settings")

            recover { exception ->
                updateState {
                    OTrackerState.Error(errorMapper.map(exception = exception))
                }
                null
            }

            init {
                settingsStorage.save(
                    listOf(
                        SettingItem(
                            key = SettingKey.NOTIFICATION,
                            isChecked = false,
                            title = "Notifications"
                        )
                    )
                )

                loadSettings()
            }

            reduce { intent ->
                when (intent) {
                    is SettingsIntent.Logout -> {
                        userStore.deleteUserId()
                    }

                    SettingsIntent.TryAgain -> loadSettings()
                    is SettingsIntent.UpdateSettingItem -> updateSetting(intent.item)
                }
            }
        }

    private suspend fun PipelineContext<OTrackerState<SettingsState>, SettingsIntent, SettingsAction>.loadSettings() {
        updateState { OTrackerState.Loading }
        runCatching { settingsStorage.getAll() }.fold(
            onSuccess = { settings ->
                updateState { OTrackerState.Success(SettingsState(settings.toUi())) }
            },
            onFailure = {
                updateState { OTrackerState.Error(errorMapper.map(exception = it)) }
            }
        )
    }

    private suspend fun PipelineContext<OTrackerState<SettingsState>, SettingsIntent, SettingsAction>.updateSetting(
        item: UiSettingItem
    ) {
        when (item.key) {
            SettingKey.NOTIFICATION -> manageReminder(item)

            SettingKey.DARK_MODE -> {
                // TODO()
            }
        }
        runCatching {
            settingsStorage.updateSetting(item.toDomain()) // todo remove returning settings
        }.onFailure { error ->
            action(SettingsAction.ShowSnackbar(error.message.orEmpty()))
        }
    }

    private suspend fun PipelineContext<OTrackerState<SettingsState>, SettingsIntent, SettingsAction>.manageReminder(
        item: UiSettingItem
    ) {
        if (!item.isChecked) {
            if (!commonNotificationManager.areNotificationsEnabled()) {
                action(SettingsAction.ShowSnackbar("Notification are not enabled"))// TODO()
                return
            }

            commonNotificationsScheduler.schedule(
                REMINDER_NOTIFICATION_ID,
                ScheduleIntervals.ONE_DAY,
                System.currentTimeMillis() + 3000
              //  elapsedRealtimeProvider() + 5000 // TODO()
            )

            // show dialog window
            // select time intent
            // setup notification
        } else {
            commonNotificationsScheduler.removeScheduling(REMINDER_NOTIFICATION_ID)
        }
    }
}

