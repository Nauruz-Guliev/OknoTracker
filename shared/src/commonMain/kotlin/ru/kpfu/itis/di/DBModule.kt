package ru.kpfu.itis.di

import org.koin.dsl.module
import ru.kpfu.itis.features.task.data.store.UserStore

fun dbModule() = module {
    single {
        UserStore(get())
    }
}