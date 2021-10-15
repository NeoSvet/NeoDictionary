package ru.neosvet.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbrains.ru.utils.network.OnlineObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import ru.neosvet.dictionary.Schedulers
import ru.neosvet.dictionary.data.IDictionarySource
import ru.neosvet.dictionary.data.storage.DicStorage
import ru.neosvet.dictionary.entries.*

class DictionaryViewModel(
    private val observer: OnlineObserver,
    private val source: IDictionarySource,
    private val storage: DicStorage
) : ViewModel(), IDictionaryViewModel {
    private val _result: MutableLiveData<DictionaryState.Model> = MutableLiveData()
    override val result: LiveData<DictionaryState.Model>
        get() = _result
    override var word: String? = null
        private set
    private val scope = CoroutineScope(
        Dispatchers.IO
                + CoroutineExceptionHandler { _, throwable ->
            onError(throwable)
        })
    private var task: Disposable? = null
    private var connector: Job? = null

    override fun searchWord(word: String, language: String) {
        this.word = word.lowercase().also { word ->
            saveWord(word)

            if (observer.isOnline.value) {
                startSearch(word, language)
            } else {
                connector = scope.launch {
                    observer.isOnline.collect {
                        if (it)
                            startSearch(word, language)
                        else
                            onError(observer.exception)
                    }
                }
            }
        }
    }

    private fun startSearch(word: String, language: String) {
        _result.postValue(
            DictionaryState.Start
        )
        task = source.searchWord(word, language)
            .subscribeOn(Schedulers.background())
            .observeOn(Schedulers.main())
            .subscribe(
                this::onSuccess,
                this::onError
            )
    }

    override fun getWords(constraint: String) {
        scope.launch {
            val words = storage.wordDao.getAll("%$constraint%")
            _result.postValue(
                DictionaryState.Words(
                    words = words
                )
            )
        }
    }

    override fun openWord(word: WordItem) {
        this.word = word.word
        scope.launch {
            val info = storage.infoDao.get(word.id)
            val list = mutableListOf<ResultItem>()
            info.forEach {
                list.add(
                    ResultItem(
                        title = it.title,
                        url = it.url,
                        description = it.description
                    )
                )
            }
            _result.postValue(
                DictionaryState.Results(
                    list = list
                )
            )
        }
    }

    override fun deleteWord(wordId: Int) {
        scope.launch {
            storage.wordDao.delete(wordId)
        }
    }

    private fun saveWord(word: String) {
        scope.launch {
            storage.wordDao.insert(
                WordItem(
                    word = word,
                    time = System.currentTimeMillis()
                )
            )
        }
    }

    private fun onSuccess(list: List<ResultItem>) {
        connector?.cancel()
        saveResult(list)
        _result.postValue(
            DictionaryState.Results(
                list = list
            )
        )
    }

    private fun saveResult(result: List<ResultItem>) {
        val word = this.word ?: return
        scope.launch {
            val item = storage.wordDao.get(word)
            if (item == null) return@launch
            val list = mutableListOf<InfoItem>()
            result.forEach {
                list.add(
                    InfoItem(
                        id = 0,
                        wordId = item.id,
                        title = it.title,
                        url = it.url,
                        description = it.description
                    )
                )
            }
            storage.infoDao.insert(list)
        }
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()
        _result.postValue(
            DictionaryState.Error(
                error = t
            )
        )
    }

    override fun onCleared() {
        task?.dispose()
        scope.cancel()
        super.onCleared()
    }
}
