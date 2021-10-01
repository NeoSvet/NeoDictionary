package ru.neosvet.neoflickr.entries

object ImagesState {
    interface Model {
        val state: State
    }

    enum class State {
        IMAGES, ERROR
    }

    data class Images(
        override val state: State = State.IMAGES,
        val urls: List<String>
    ) : Model

    data class Error(
        override val state: State = State.ERROR,
        val error: Throwable
    ) : Model
}