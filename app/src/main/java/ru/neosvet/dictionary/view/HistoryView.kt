package ru.neosvet.dictionary.view

interface HistoryView {
    fun openWord(wordId: Int)
    fun onError(t: Throwable)
}