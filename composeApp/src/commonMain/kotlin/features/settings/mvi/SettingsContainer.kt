package features.settings.mvi


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
import ru.kpfu.itis.features.notifications.ONotification
import ru.kpfu.itis.features.notifications.ScheduleIntervals
import ru.kpfu.itis.features.settings.data.SettingStorage
import ru.kpfu.itis.features.settings.domain.SettingItem
import ru.kpfu.itis.features.settings.domain.SettingKey
import ru.kpfu.itis.features.task.data.store.UserStore
import java.util.Calendar

class SettingsContainer(
    private val userStore: UserStore,
    private val configurationFactory: ConfigurationFactory,
    private val errorMapper: ErrorMapper,
    private val settingsStorage: SettingStorage,
    private val commonNotificationManager: CommonNotificationManager,
    private val commonNotificationsScheduler: CommonNotificationsScheduler,
    private val reminderNotification: ONotification
) : Container<SettingsState, SettingsIntent, SettingsAction> {

    override val store: Store<SettingsState, SettingsIntent, SettingsAction> =
        store(SettingsState.Initial) {

            configure(configurationFactory, "Settings")

            recover { exception ->
                updateState {
                    SettingsState.Error(errorMapper.map(exception = exception))
                }
                null
            }

            init {
                // TODO() remove
                settingsStorage.save(
                    listOf(
                        SettingItem(
                            key = SettingKey.NOTIFICATION,
                            isChecked = false,
                            title = "Notifications"
                        )
                    )
                )
                //

                loadSettings()
            }

            reduce { intent ->
                when (intent) {
                    is SettingsIntent.Logout -> {
                        userStore.deleteUserId()
                        action(SettingsAction.Logout)
                    }

                    SettingsIntent.TryAgain -> loadSettings()
                    is SettingsIntent.UpdateSettingItem -> updateSetting(intent.item)
                    is SettingsIntent.NotificationTimeWasSelected -> scheduleDailyNotification(
                        intent.hour,
                        intent.minute
                    )

                    SettingsIntent.TimePickerDialogWasClosed -> {
                        action(SettingsAction.HideTimePicker)
                    }
                }
            }
        }

    private suspend fun PipelineContext<SettingsState, SettingsIntent, SettingsAction>.loadSettings() {
        updateState { SettingsState.Loading }
        runCatching { settingsStorage.getAll() }.fold(
            onSuccess = { settings ->
                updateState { SettingsState.SettingsDisplay(settings.toUi()) }
            },
            onFailure = {
                updateState { SettingsState.Error(errorMapper.map(exception = it)) }
            }
        )
    }

    private suspend fun PipelineContext<SettingsState, SettingsIntent, SettingsAction>.updateSetting(
        item: UiSettingItem
    ) {
        when (item.key) {
            SettingKey.NOTIFICATION -> manageReminder(item)

            SettingKey.DARK_MODE -> {
                // TODO()
            }
        }
        runCatching {
            settingsStorage.updateSetting(item.toDomain())
        }.onFailure { error ->
            action(SettingsAction.ShowSnackbar(error.message.orEmpty()))
        }.onSuccess { settings ->
            updateState { SettingsState.SettingsDisplay(settings.toUi()) }
        }
    }

    private suspend fun PipelineContext<SettingsState, SettingsIntent, SettingsAction>.manageReminder(
        item: UiSettingItem
    ) {
        if (!item.isChecked) {
            if (!commonNotificationManager.areNotificationsEnabled()) {
                action(SettingsAction.ShowSnackbar("Notification are not enabled"))// TODO()
                return
            }
            action(SettingsAction.ShowTimePicker)
        } else {
            commonNotificationsScheduler.removeScheduling(REMINDER_NOTIFICATION_ID)
        }
    }

    private suspend fun PipelineContext<SettingsState, SettingsIntent, SettingsAction>.scheduleDailyNotification(
        hour: Int,
        minute: Int
    ) {
        action(SettingsAction.HideTimePicker)
        val now = Calendar.getInstance()
        val nextNotificationTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (now.after(nextNotificationTime)) {
            nextNotificationTime.add(Calendar.DAY_OF_YEAR, 1)
        }
        // todo
        reminderNotification.show()

        commonNotificationsScheduler.removeScheduling(REMINDER_NOTIFICATION_ID)
        commonNotificationsScheduler.schedule(
            REMINDER_NOTIFICATION_ID,
            ScheduleIntervals.ONE_DAY,
            nextNotificationTime.timeInMillis
        )
    }

}

