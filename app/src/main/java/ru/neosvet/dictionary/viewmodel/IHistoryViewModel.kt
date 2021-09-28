package ru.neosvet.dictionary.viewmodel

import androidx.lifecycle.LiveData
import ru.neosvet.dictionary.entries.HistoryState
import ru.neosvet.dictionary.entries.WordItem

interface IHistoryViewModel {
    val result: LiveData<HistoryState.Model>
    fun getWords()
    fun deleteWord(word: WordItem)
    fun clearHistory()
}