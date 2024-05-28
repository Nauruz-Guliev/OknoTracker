package ru.kpfu.itis.di

import org.koin.dsl.module
import ru.kpfu.itis.features.attachments.service.AttachmentService
import ru.kpfu.itis.features.task.data.service.TaskService
import ru.kpfu.itis.features.user.data.service.UserService

fun serviceModule() = module {
    factory {
        AttachmentService(get(), get())
    }
    factory {
        UserService(get(), get())
    }
    factory {
        TaskService(get(), get())
    }
}