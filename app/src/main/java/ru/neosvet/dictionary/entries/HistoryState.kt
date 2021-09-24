package ru.neosvet.dictionary.entries

object HistoryState {
    interface Model {
        val state: State
    }

    enum class State {
        WORDS, ERROR
    }

    data class Words(
        override val state: State = State.WORDS,
        val words: List<WordItem>
    ) : Model

    data class Error(
        override val state: State = State.ERROR,
        val error: Throwable
    ) : Model
}