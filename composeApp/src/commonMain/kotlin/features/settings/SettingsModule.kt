package features.settings

import org.koin.dsl.module

fun settingsModule() = module {
    single {
        SettingsContainer(get(), get(), get())
    }
}