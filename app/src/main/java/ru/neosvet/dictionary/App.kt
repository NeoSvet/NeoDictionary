package ru.neosvet.dictionary

import android.app.Application
import org.koin.core.context.startKoin
import ru.neosvet.dictionary.data.storage.DicStorage
import ru.neosvet.dictionary.di.KoinModule
import ru.neosvet.dictionary.entries.DicStrings

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val strings = DicStrings(
            word = getString(R.string.word),
            phonetics = getString(R.string.phonetics),
            meanings = getString(R.string.meanings),
            partOfSpeech = getString(R.string.partOfSpeech),
            definition = getString(R.string.definition),
            example = getString(R.string.example),
            synonyms = getString(R.string.synonyms),
            antonyms = getString(R.string.antonyms)
        )
        val storage = DicStorage.get(applicationContext)
        startKoin {
            modules(KoinModule.create(strings, storage))
        }
    }
}