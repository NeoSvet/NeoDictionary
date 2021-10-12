package ru.neosvet.app.view.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.neosvet.dictionary.entries.WordItem
import ru.neosvet.app.view.DictionaryFragment

object DictionaryScreen {
    fun create(word: WordItem? = null) = FragmentScreen { DictionaryFragment.newInstance(word) }
}