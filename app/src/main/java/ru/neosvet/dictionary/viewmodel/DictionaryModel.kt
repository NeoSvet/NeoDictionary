package ru.neosvet.dictionary.viewmodel

interface DictionaryModel {
    val word: String?
    fun searchWord(word: String, language: String)
}