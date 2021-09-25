package ru.neosvet.dictionary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.neosvet.dictionary.data.storage.DicStorage
import ru.neosvet.dictionary.entries.HistoryState
import ru.neosvet.dictionary.entries.WordItem

class HistoryViewModel(
    private val storage: DicStorage
) : ViewModel(), IHistoryViewModel {
    private val _result: MutableLiveData<HistoryState.Model> = MutableLiveData()
    override val result: LiveData<HistoryState.Model>
        get() = _result
    private val scope = CoroutineScope(
        Dispatchers.IO
                + CoroutineExceptionHandler { _, throwable ->
            onError(throwable)
        })

    override fun getWords() {
        scope.launch {
            _result.postValue(
                HistoryState.Words(
                    words = storage.wordDao.getAll()
                )
            )
        }
    }

    override fun deleteWord(word: WordItem) {
        scope.launch {
            storage.wordDao.delete(word)
        }
    }

    override fun clearHistory() {
        scope.launch {
            storage.wordDao.deleteAll()
        }
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()
        _result.postValue(
            HistoryState.Error(
                error = t
            )
        )
    }

    override fun onCleared() {
        scope.cancel()
        super.onCleared()
    }
}