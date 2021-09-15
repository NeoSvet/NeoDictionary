package ru.neosvet.dictionary

import android.app.Application
import ru.neosvet.dictionary.data.MainSource
import ru.neosvet.dictionary.data.client.Client
import ru.neosvet.dictionary.entries.DicStrings
import ru.neosvet.dictionary.presenter.DictionaryPresenter
import ru.neosvet.dictionary.presenter.ListPresenter
import ru.neosvet.dictionary.presenter.MainPresenter

class App : Application() {
    lateinit var dictionary: DictionaryPresenter

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
        val source = MainSource(Client.instance, strings)
        val list = ListPresenter()
        dictionary = MainPresenter(source, list)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}