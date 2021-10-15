package ru.neosvet.dictionary.entries

object HistoryState {
    interface Model

    data class Words(
        val words: List<WordItem>
    ) : Model

    data class Error(
        val error: Throwable
    ) : Model
}