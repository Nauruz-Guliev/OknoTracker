package features.tasks.main.di

import features.tasks.main.mvi.MainContainer
import org.koin.dsl.module

fun mainTasksModule() = module {
    single {
        MainContainer(
            errorMapper = get(),
            configurationFactory = get(),
            repository = get(),
            userStore = get()
        )
    }
}