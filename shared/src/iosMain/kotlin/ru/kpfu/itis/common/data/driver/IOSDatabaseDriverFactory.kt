package ru.kpfu.itis.common.data.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import ru.kpfu.itis.features.task.TaskDatabase

class IOSDatabaseDriverFactory : DatabaseDriverFactory {

    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(TaskDatabase.Schema, "tracker.db")
    }
}