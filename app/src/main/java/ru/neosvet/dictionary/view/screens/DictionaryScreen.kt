package ru.neosvet.dictionary.view.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.neosvet.dictionary.view.DictionaryFragment

object DictionaryScreen {
    fun create(wordId: Int = 0) = FragmentScreen { DictionaryFragment.newInstance(wordId) }
}