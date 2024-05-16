package ru.kpfu.itis

import android.app.Application
import features.signin.signInModule
import initKoin
import org.koin.android.ext.koin.androidContext
import ru.kpfu.itis.di.androidModule
import ru.kpfu.itis.di.networkModule

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(
            signInModule(),
            networkModule(),
            androidModule(),
        ) {
            androidContext(this@Application)
        }
    }
}