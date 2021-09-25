package ru.neosvet.dictionary

import android.app.Application
import ru.neosvet.dictionary.data.DictionarySource
import ru.neosvet.dictionary.data.IDictionarySource
import ru.neosvet.dictionary.data.client.Client
import ru.neosvet.dictionary.entries.DicStrings

class App : Application() {
    lateinit var source: IDictionarySource

    override fun onCreate() {
        super.onCreate()
        instance = this
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
        source = DictionarySource(Client.instance, strings)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}