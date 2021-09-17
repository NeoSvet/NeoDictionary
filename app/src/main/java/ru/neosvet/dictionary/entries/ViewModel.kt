package ru.neosvet.dictionary.entries

open class ModelResult(
    val state: StateResult
)

enum class StateResult {
    LIST, ERROR
}

class ListResult(
    val list: List<ResultItem>
) : ModelResult(StateResult.LIST)

class ErrorResult(
    val error: Throwable
) : ModelResult(StateResult.ERROR)