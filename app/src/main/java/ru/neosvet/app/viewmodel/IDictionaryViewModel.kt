package ru.neosvet.app.viewmodel

import androidx.lifecycle.LiveData
import ru.neosvet.dictionary.entries.DictionaryState
import ru.neosvet.dictionary.entries.WordItem

interface IDictionaryViewModel {
    val result: LiveData<DictionaryState.Model>
    val word: String?
    fun searchWord(word: String, language: String)
    fun getWords(constraint: String)
    fun openWord(word: WordItem)
    fun deleteWord(wordId: Int)
}