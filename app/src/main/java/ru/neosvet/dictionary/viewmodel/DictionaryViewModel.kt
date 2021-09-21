package ru.neosvet.dictionary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.neosvet.dictionary.Schedulers
import ru.neosvet.dictionary.data.IDictionarySource
import ru.neosvet.dictionary.entries.ErrorResult
import ru.neosvet.dictionary.entries.ListResult
import ru.neosvet.dictionary.entries.ModelResult
import ru.neosvet.dictionary.entries.ResultItem

class DictionaryViewModel(
    private val source: IDictionarySource
) : ViewModel(), IDictionaryViewModel {
    private val _result: MutableLiveData<ModelResult> = MutableLiveData()
    override val result: LiveData<ModelResult>
        get() = _result
    override var word: String? = null
        private set

    override fun searchWord(word: String, language: String) {
        this.word = word

        source.searchWord(word, language)
            .subscribeOn(Schedulers.background())
            .observeOn(Schedulers.main())
            .subscribe(
                this::onSuccess,
                this::onError
            )
    }

    private fun onSuccess(list: List<ResultItem>) {
        _result.postValue(
            ListResult(
                list = list
            )
        )
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()
        _result.postValue(
            ErrorResult(
                error = t
            )
        )
    }
}