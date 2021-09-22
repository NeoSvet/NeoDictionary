package ru.neosvet.dictionary.entries

interface ModelResult {
    val state: StateResult
}

enum class StateResult {
    LIST, WORDS, ERROR
}

data class ListResult(
    override val state: StateResult = StateResult.LIST,
    val list: List<ResultItem>
) : ModelResult

data class WordsResult(
    override val state: StateResult = StateResult.WORDS,
    val words: List<WordItem>
) : ModelResult

data class ErrorResult(
    override val state: StateResult = StateResult.ERROR,
    val error: Throwable
) : ModelResult