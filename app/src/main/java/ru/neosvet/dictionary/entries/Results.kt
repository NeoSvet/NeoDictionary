package ru.neosvet.dictionary.entries

interface ModelResult {
    val state: StateResult
}

enum class StateResult {
    LIST, ERROR
}

data class ListResult(
    override val state: StateResult = StateResult.LIST,
    val list: List<ResultItem>
) : ModelResult

data class ErrorResult(
    override val state: StateResult = StateResult.ERROR,
    val error: Throwable
) : ModelResult