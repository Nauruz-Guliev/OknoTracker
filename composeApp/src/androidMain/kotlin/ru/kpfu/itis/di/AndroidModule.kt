package ru.kpfu.itis.di

import org.koin.dsl.module
import ru.kpfu.itis.common.data.driver.AndroidDatabaseDriverFactory
import ru.kpfu.itis.common.driver.DatabaseDriverFactory
import ru.kpfu.itis.utils.Strings

fun androidModule() = module {
    factory {
        Strings(get())
    }
    factory<DatabaseDriverFactory> {
        AndroidDatabaseDriverFactory(get())
    }
}