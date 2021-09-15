package ru.neosvet.dictionary.presenter

import ru.neosvet.dictionary.Schedulers
import ru.neosvet.dictionary.data.DictionarySource
import ru.neosvet.dictionary.entries.ResultItem
import ru.neosvet.dictionary.view.DictionaryView

class MainPresenter(
    private val source: DictionarySource,
    private val list: IListPresenter
) : DictionaryPresenter {
    private var view: DictionaryView? = null
    override var word: String? = null
        private set

    override fun attachView(view: DictionaryView) {
        this.view = view
        list.setView(view)
        if (list.getCount() > 0)
            view.updateList(list)
    }

    override fun detachView() {
        view = null
    }

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

    private fun onSuccess(items: List<ResultItem>) {
        list.setItems(items)
        view?.updateList(list)
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()
        view?.onError(t)
    }
}