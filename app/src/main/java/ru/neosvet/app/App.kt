package ru.neosvet.app

import android.app.Application
import org.koin.core.context.startKoin
import ru.neosvet.app.di.KoinModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(KoinModule.create(applicationContext))
        }
    }
}