package ru.neosvet.dictionary.presenter

import ru.neosvet.dictionary.view.DictionaryView

interface DictionaryPresenter {
    val word: String?
    fun attachView(view: DictionaryView)
    fun detachView()
    fun searchWord(word: String, language: String)
}