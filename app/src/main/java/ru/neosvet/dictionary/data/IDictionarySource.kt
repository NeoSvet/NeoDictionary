package ru.neosvet.dictionary.data

import io.reactivex.rxjava3.core.Single
import ru.neosvet.dictionary.entries.ResultItem

interface IDictionarySource {
    fun searchWord(word: String, language: String): Single<List<ResultItem>>
}