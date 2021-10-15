package ru.neosvet.app.entries

import ru.neosvet.dictionary.entries.WordItem

object HistoryState {
    interface Model

    data class Words(
        val words: List<WordItem>
    ) : Model

    data class Error(
        val error: Throwable
    ) : Model
}