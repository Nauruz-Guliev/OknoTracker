package ru.kpfu.itis.di

import org.koin.dsl.module
import ru.kpfu.itis.features.task.data.db.AttachmentDbImpl
import ru.kpfu.itis.features.task.data.db.TaskDbImpl
import ru.kpfu.itis.features.task.data.store.UserStore

fun dbModule() = module {
    single {
        UserStore(get(), get())
    }
    single {
        TaskDbImpl(get(), get())
    }
    single {
        AttachmentDbImpl(get())
    }
}