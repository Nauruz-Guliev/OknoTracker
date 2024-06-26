package features.settings.di

import features.settings.mvi.SettingsContainer
import org.koin.dsl.module
import ru.kpfu.itis.features.notifications.CommonNotificationManager
import ru.kpfu.itis.features.notifications.CommonNotificationsScheduler
import ru.kpfu.itis.features.settings.data.SettingStorage

fun settingsModule() = module {
    single {
        SettingsContainer(
            userStore = get(),
            configurationFactory = get(),
            errorMapper = get(),
            settingsStorage = get(),
            commonNotificationManager = get<CommonNotificationManager>(),
            commonNotificationsScheduler = get<CommonNotificationsScheduler>(),
            get()
        )
    }
    single {
        SettingStorage(
            get()
        )
    }
}