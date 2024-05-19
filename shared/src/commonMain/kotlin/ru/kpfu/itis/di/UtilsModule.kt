package ru.kpfu.itis.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

fun dispatcherModule() = module {
    factory<CoroutineDispatcher> {
        Dispatchers.IO
    }
}