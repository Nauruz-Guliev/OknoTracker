package ru.kpfu.itis

import android.app.Application
import features.signin.signInModule
import features.tasks.tasksModule
import initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.kpfu.itis.common.data.driver.databaseModule
import ru.kpfu.itis.di.androidModule
import ru.kpfu.itis.di.dbModule
import ru.kpfu.itis.di.networkModule

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(
            signInModule(),
            networkModule(),
            androidModule(),
            tasksModule(),
            databaseModule(),
            dbModule(),
            kstoreModule(cacheDir.path),
        ) {
            androidContext(this@Application)
        }
    }
}

fun kstoreModule(filePath: String) = module { factory { filePath } }