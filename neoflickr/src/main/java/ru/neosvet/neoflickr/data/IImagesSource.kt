package ru.neosvet.neoflickr.data

import io.reactivex.rxjava3.core.Single

interface IImagesSource {
    fun search(query: String): Single<List<String>>
}