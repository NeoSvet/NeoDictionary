package ru.neosvet.dictionary.viewmodel

import androidx.lifecycle.LiveData
import ru.neosvet.dictionary.entries.ModelResult

interface IDictionaryViewModel {
    val result: LiveData<ModelResult>
    val word: String?
    fun searchWord(word: String, language: String)
    fun getWords(constraint: String)
    fun openWord(wordId: Int)
    fun deleteWord(wordId: Int)
}