package ru.kpfu.itis.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module
import ru.kpfu.itis.common.mapper.ErrorMapper

fun dispatcherModule() = module {
    factory<CoroutineDispatcher> {
        Dispatchers.IO
    }
    factory {
        ErrorMapper(get())
    }
}