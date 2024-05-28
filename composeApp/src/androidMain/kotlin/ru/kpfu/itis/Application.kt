package ru.kpfu.itis

import android.app.Application
import features.settings.di.settingsModule
import features.signin.di.signInModule
import features.statistics.di.statisticsModule
import features.tasks.tasksModule
import initKoin
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.kpfu.itis.common.data.driver.databaseModule
import ru.kpfu.itis.di.androidModule
import ru.kpfu.itis.di.dbModule
import ru.kpfu.itis.di.dispatcherModule
import ru.kpfu.itis.di.networkModule
import ru.kpfu.itis.di.notifications.notificationsModule
import ru.kpfu.itis.di.repositoryModule
import ru.kpfu.itis.di.serviceModule
import ru.kpfu.itis.features.notifications.AndroidNotificationChannelProvider

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
            settingsModule(),
            dispatcherModule(),
            statisticsModule(),
            repositoryModule(),
            serviceModule(),
            notificationsModule(),
        ) {
            androidContext(this@Application)
        }

        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        val notificationProvider: AndroidNotificationChannelProvider by inject()
        notificationProvider.createNotificationChannel(
            channelId = getString(OResources.Notification.notificationChannelId().resourceId),
            channelName = getString(OResources.Notification.notificationChannelName().resourceId),
            channelDescription = getString(OResources.Notification.notificationChannelDescription().resourceId),
        )
    }

}

fun kstoreModule(filePath: String) = module { factory { filePath } }
