package ru.neosvet.app.view.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.neosvet.app.view.HistoryFragment

object HistoryScreen {
    fun create() = FragmentScreen { HistoryFragment() }
}