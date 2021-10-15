package ru.neosvet.dictionary.view.list

import ru.neosvet.dictionary.entries.ResultItem

interface ListView {
    var pos: Int
    fun setItem(item: ResultItem)
}