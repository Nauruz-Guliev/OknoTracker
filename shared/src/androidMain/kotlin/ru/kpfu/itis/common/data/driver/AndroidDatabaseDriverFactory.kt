package ru.kpfu.itis.common.data.driver

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.dsl.module
import ru.kpfu.itis.common.driver.DatabaseDriverFactory
import ru.kpfu.itis.features.task.TaskDatabase

class AndroidDatabaseDriverFactory(private val context: Context) : DatabaseDriverFactory {

    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(TaskDatabase.Schema, context, "tracker.db")
    }
}

fun databaseModule() = module {
    factory<DatabaseDriverFactory> {
        AndroidDatabaseDriverFactory(get())
    }
}