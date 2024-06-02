package features.tasks.home.di

import features.tasks.home.mvi.HomeTasksContainer
import org.koin.dsl.module

fun homeTasksModule() = module {
    single {
        HomeTasksContainer(
            errorMapper = get(),
            repository = get(),
            configurationFactory = get(),
            userStore = get()
        )
    }
}