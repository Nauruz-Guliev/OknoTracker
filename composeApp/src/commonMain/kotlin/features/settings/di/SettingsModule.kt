package features.settings.di

import features.settings.ReminderNotification
import features.settings.mvi.SettingsContainer
import org.koin.dsl.module
import ru.kpfu.itis.features.settings.data.SettingStorage

fun settingsModule() = module {
    single {
        SettingsContainer(get(), get(), get(), get(), ReminderNotification())
    }
    single {
        SettingStorage(
            get()
        )
    }
}