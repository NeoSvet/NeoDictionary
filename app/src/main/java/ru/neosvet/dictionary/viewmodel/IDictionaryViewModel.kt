package ru.neosvet.dictionary.viewmodel

interface IDictionaryViewModel {
    val word: String?
    fun searchWord(word: String, language: String)
}