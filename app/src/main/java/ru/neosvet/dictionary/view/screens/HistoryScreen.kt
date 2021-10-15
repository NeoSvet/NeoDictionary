package ru.neosvet.dictionary.view.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.neosvet.dictionary.view.HistoryFragment

object HistoryScreen {
    fun create() = FragmentScreen { HistoryFragment() }
}