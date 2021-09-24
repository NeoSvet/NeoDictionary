package ru.neosvet.dictionary.entries

object DictionaryState {
    interface Model {
        val state: State
    }

    enum class State {
        RESULTS, WORDS, ERROR
    }

    data class Results(
        override val state: State = State.RESULTS,
        val list: List<ResultItem>
    ) : Model

    data class Words(
        override val state: State = State.WORDS,
        val words: List<WordItem>
    ) : Model

    data class Error(
        override val state: State = State.ERROR,
        val error: Throwable
    ) : Model
}