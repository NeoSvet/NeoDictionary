package ru.neosvet.dictionary.viewmodel

import androidx.lifecycle.LiveData
import ru.neosvet.dictionary.entries.ImagesState

interface IImagesViewModel {
    val result: LiveData<ImagesState.Model>
    fun search(query: String)
}