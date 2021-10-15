package ru.neosvet.neoflickr.viewmodel

import androidx.lifecycle.LiveData
import geekbrains.ru.utils.network.OnlineObserver
import ru.neosvet.neoflickr.entries.ImagesState

interface IImagesViewModel {
    val result: LiveData<ImagesState.Model>
    fun search(query: String, observer: OnlineObserver)
}