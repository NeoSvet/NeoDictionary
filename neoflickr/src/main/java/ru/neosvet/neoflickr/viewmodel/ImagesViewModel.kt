package ru.neosvet.neoflickr.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbrains.ru.utils.network.OnlineObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import ru.neosvet.neoflickr.Schedulers
import ru.neosvet.neoflickr.data.IImagesSource
import ru.neosvet.neoflickr.data.ImagesSource
import ru.neosvet.neoflickr.data.client.ImgClient
import ru.neosvet.neoflickr.entries.ImagesState

class ImagesViewModel : ViewModel(), IImagesViewModel {
    private val source: IImagesSource = ImagesSource(ImgClient.create())
    private val _result: MutableLiveData<ImagesState.Model> = MutableLiveData()
    override val result: LiveData<ImagesState.Model>
        get() = _result
    private val scope = CoroutineScope(
        Dispatchers.IO
    )
    private var task: Disposable? = null
    private var connector: Job? = null

    override fun search(query: String, observer: OnlineObserver) {
        observer.onActive()
        if (observer.isOnline.value) {
            startSearch(query)
        } else {
            connector = scope.launch {
                observer.isOnline.collect {
                    if (it)
                        startSearch(query)
                    else
                        onError(observer.exception)
                }
            }
        }
    }

    private fun startSearch(query: String) {
        _result.postValue(
            ImagesState.Start
        )
        task = source.search(query)
            .subscribeOn(Schedulers.background())
            .observeOn(Schedulers.main())
            .subscribe(
                this::onSuccess,
                this::onError
            )
    }

    private fun onSuccess(list: List<String>) {
        connector?.cancel()
        _result.postValue(
            ImagesState.Images(
                urls = list
            )
        )
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()
        _result.postValue(
            ImagesState.Error(
                error = t
            )
        )
    }

    override fun onCleared() {
        scope.cancel()
        task?.dispose()
        super.onCleared()
    }
}