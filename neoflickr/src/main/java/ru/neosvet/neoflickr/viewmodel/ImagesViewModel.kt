package ru.neosvet.neoflickr.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
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
    private var task: Disposable? = null

    override fun search(query: String) {
        task = source.search(query)
            .subscribeOn(Schedulers.background())
            .observeOn(Schedulers.main())
            .subscribe(
                this::onSuccess,
                this::onError
            )
    }

    private fun onSuccess(list: List<String>) {
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
        task?.dispose()
        super.onCleared()
    }
}