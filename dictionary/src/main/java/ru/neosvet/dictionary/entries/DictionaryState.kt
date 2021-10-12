package ru.neosvet.dictionary.entries

object DictionaryState {
    interface Model

    object Start : Model

    data class Results(
        val list: List<ResultItem>
    ) : Model

    data class Words(
        val words: List<WordItem>
    ) : Model

    data class Error(
        val error: Throwable
    ) : Model
}