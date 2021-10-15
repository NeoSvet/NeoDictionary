package ru.neosvet.dictionary.view

import ru.neosvet.dictionary.presenter.IListPresenter

interface DictionaryView {
    fun openUrl(url: String)
    fun updateList(list: IListPresenter)
    fun onError(t: Throwable)
}