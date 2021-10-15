package ru.neosvet.dictionary.presenter

import ru.neosvet.dictionary.entries.ResultItem
import ru.neosvet.dictionary.view.DictionaryView
import ru.neosvet.dictionary.view.list.ListView

interface IListPresenter {
    var itemClickListener: ((ListView) -> Unit)?
    fun setView(view: DictionaryView)
    fun setItems(items: List<ResultItem>)
    fun bindView(view: ListView)
    fun getCount(): Int
    fun isTitle(position: Int): Boolean
}