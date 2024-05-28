package ru.kpfu.itis.di

import org.koin.dsl.module
import ru.kpfu.itis.features.attachments.repository.AttachmentRepository
import ru.kpfu.itis.features.statistics.StatisticsRepository
import ru.kpfu.itis.features.task.data.repository.TaskRepository
import ru.kpfu.itis.features.user.data.repository.UserRepository

fun repositoryModule() = module {
    factory {
        TaskRepository(get(), get(), get(), get(), get())
    }
    factory {
        UserRepository(get(), get())
    }
    factory {
        AttachmentRepository(get(), get(), get(), get())
    }
    factory {
        StatisticsRepository(
            statisticsService = get(),
            mapper = get(),
            userStore = get()
        )
    }
}