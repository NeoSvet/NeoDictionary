package ru.neosvet.dictionary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.neosvet.dictionary.Schedulers
import ru.neosvet.dictionary.data.IDictionarySource
import ru.neosvet.dictionary.data.storage.DicStorage
import ru.neosvet.dictionary.entries.*

class DictionaryViewModel(
    private val source: IDictionarySource,
    private val storage: DicStorage
) : ViewModel(), IDictionaryViewModel {
    private val _result: MutableLiveData<ModelResult> = MutableLiveData()
    override val result: LiveData<ModelResult>
        get() = _result
    override var word: String? = null
        private set
    private val scope = CoroutineScope(
        Dispatchers.IO
                + CoroutineExceptionHandler { _, throwable ->
            onError(throwable)
        })

    override fun searchWord(word: String, language: String) {
        this.word = word.lowercase().also {
            saveWord(it)

            source.searchWord(it, language)
                .subscribeOn(Schedulers.background())
                .observeOn(Schedulers.main())
                .subscribe(
                    this::onSuccess,
                    this::onError
                )
        }
    }

    override fun getWords(constraint: String) {
        scope.launch {
            val words = storage.wordDao.getAll("%$constraint%")
            _result.postValue(
                WordsResult(
                    words = words
                )
            )
        }
    }

    override fun openWord(wordId: Int) {
        scope.launch {
            val info = storage.infoDao.get(wordId)
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
                ListResult(
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
            if (storage.wordDao.get(word) == null)
                storage.wordDao.insert(WordItem(0, word))
        }
    }

    private fun onSuccess(list: List<ResultItem>) {
        saveResult(list)
        _result.postValue(
            ListResult(
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
            ErrorResult(
                error = t
            )
        )
    }
}