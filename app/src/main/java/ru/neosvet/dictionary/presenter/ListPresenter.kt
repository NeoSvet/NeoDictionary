package ru.neosvet.dictionary.presenter

import ru.neosvet.dictionary.entries.ResultItem
import ru.neosvet.dictionary.view.DictionaryView
import ru.neosvet.dictionary.view.list.ListView

class ListPresenter : IListPresenter {
    private var dictionaryView: DictionaryView? = null
    private val list = mutableListOf<ResultItem>()

    override var itemClickListener: ((ListView) -> Unit)? = { view ->
        val item = list[view.pos]
        item.url?.let {
            if (it.indexOf("http") == 0)
                dictionaryView?.openUrl(it)
            else
                dictionaryView?.openUrl("http:$it")
        }
    }

    override fun setView(view: DictionaryView) {
        dictionaryView = view
    }

    override fun setItems(items: List<ResultItem>) {
        list.clear()
        list.addAll(items)
    }

    override fun bindView(view: ListView) {
        val item = list[view.pos]
        view.setItem(item)
    }

    override fun getCount() = list.size

    override fun isTitle(position: Int) = list[position].title != null
}