package ru.kpfu.itis

import android.app.Application
import features.settings.di.settingsModule
import features.signin.di.signInModule
import features.signup.di.signUpModule
import features.statistics.di.statisticsModule
import features.tasks.completed.di.completedTasksModule
import features.tasks.home.di.homeTasksModule
import features.tasks.main.di.mainTasksModule
import features.tasks.single.di.singleTaskModule
import flow_mvi.flowMviModule
import initKoin
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.kpfu.itis.di.androidModule
import ru.kpfu.itis.di.dbModule
import ru.kpfu.itis.di.dispatcherModule
import ru.kpfu.itis.di.mapperModule
import ru.kpfu.itis.di.networkModule
import ru.kpfu.itis.di.notifications.notificationsModule
import ru.kpfu.itis.di.repositoryModule
import ru.kpfu.itis.di.serviceModule
import ru.kpfu.itis.features.notifications.AndroidNotificationChannelProvider

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(
            // data modules
            networkModule(),
            androidModule(),
            serviceModule(),
            mapperModule(),
            dbModule(),
            repositoryModule(),
            kstoreModule(cacheDir.path),
            // feature modules
            signInModule(),
            settingsModule(),
            statisticsModule(),
            signUpModule(),
            completedTasksModule(),
            homeTasksModule(),
            mainTasksModule(),
            singleTaskModule(),
            // utils module
            dispatcherModule(),
            notificationsModule(),
            flowMviModule(),
        ) {
            androidContext(this@Application)
        }
        createNotificationChannel()
    }

    private fun kstoreModule(filePath: String) = module { factory { filePath } }


    private fun createNotificationChannel() {
        val notificationProvider: AndroidNotificationChannelProvider by inject()
        notificationProvider.createNotificationChannel(
            channelId = getString(OResources.Notification.notificationChannelId().resourceId),
            channelName = getString(OResources.Notification.notificationChannelName().resourceId),
            channelDescription = getString(OResources.Notification.notificationChannelDescription().resourceId),
        )
    }
}