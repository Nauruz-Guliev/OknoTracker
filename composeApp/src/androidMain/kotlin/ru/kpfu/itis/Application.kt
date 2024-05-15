package ru.kpfu.itis


import android.app.Application
import common.features.di.signInModule
import initKoin
import org.koin.android.ext.koin.androidContext
import ru.kpfu.itis.di.networkModule

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(
            signInModule(),
            networkModule()
        ) {
            androidContext(this@Application)
        }
    }
}