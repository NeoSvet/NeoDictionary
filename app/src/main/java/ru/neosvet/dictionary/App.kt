package ru.neosvet.dictionary

import android.app.Application
import org.koin.core.context.startKoin
import ru.neosvet.dictionary.di.KoinModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(KoinModule.create(applicationContext))
        }
    }
}