package ru.neosvet.dictionary.viewmodel

import androidx.lifecycle.LiveData
import ru.neosvet.dictionary.entries.DictionaryState

interface IDictionaryViewModel {
    val result: LiveData<DictionaryState.Model>
    val word: String?
    fun searchWord(word: String, language: String)
    fun getWords(constraint: String)
    fun openWord(wordId: Int)
    fun deleteWord(wordId: Int)
}