package ru.neosvet.neoflickr.entries

object ImagesState {
    interface Model

    object Start : Model

    data class Images(
        val urls: List<String>
    ) : Model

    data class Error(
        val error: Throwable
    ) : Model
}