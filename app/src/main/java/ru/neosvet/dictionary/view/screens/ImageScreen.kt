package ru.neosvet.dictionary.view.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.neosvet.neoflickr.view.ImagesFragment

object ImageScreen {
    fun create(query: String) = FragmentScreen { ImagesFragment.newInstance(query) }
}