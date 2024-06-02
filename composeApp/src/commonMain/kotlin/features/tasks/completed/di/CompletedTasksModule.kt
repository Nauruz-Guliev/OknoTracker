package features.tasks.completed.di

import features.tasks.completed.mvi.CompletedTasksContainer
import org.koin.dsl.module

fun completedTasksModule() = module {
    single {
        CompletedTasksContainer(get(), get(), get(), get())
    }
}