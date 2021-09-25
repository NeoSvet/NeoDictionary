package ru.neosvet.dictionary.view.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.neosvet.dictionary.entries.WordItem
import ru.neosvet.dictionary.view.DictionaryFragment

object DictionaryScreen {
    fun create(word: WordItem? = null) = FragmentScreen { DictionaryFragment.newInstance(word) }
}