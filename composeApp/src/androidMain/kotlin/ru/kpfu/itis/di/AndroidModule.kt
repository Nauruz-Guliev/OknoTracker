package ru.kpfu.itis.di

import org.koin.dsl.module
import ru.kpfu.itis.utils.Strings

fun androidModule() = module {
    factory {
        Strings(get())
    }
}