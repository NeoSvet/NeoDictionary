package ru.neosvet.dictionary.viewmodel

import androidx.lifecycle.ViewModel
import ru.neosvet.dictionary.App
import ru.neosvet.dictionary.Schedulers
import ru.neosvet.dictionary.entries.ErrorResult
import ru.neosvet.dictionary.entries.ListResult
import ru.neosvet.dictionary.entries.ResultItem

class DictionaryViewModel : ViewModel(), DictionaryModel {
    override var word: String? = null
        private set

    override fun searchWord(word: String, language: String) {
        this.word = word

        App.instance.source.searchWord(word, language)
            .subscribeOn(Schedulers.background())
            .observeOn(Schedulers.main())
            .subscribe(
                this::onSuccess,
                this::onError
            )
    }

    private fun onSuccess(list: List<ResultItem>) {
        App.instance.liveResult.postValue(
            ListResult(
                list = list
            )
        )
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()
        App.instance.liveResult.postValue(
            ErrorResult(
                error = t
            )
        )
    }
}