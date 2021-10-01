package ru.neosvet.dictionary.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.neosvet.dictionary.data.DictionarySource
import ru.neosvet.dictionary.data.IDictionarySource
import ru.neosvet.dictionary.data.client.DicClient
import ru.neosvet.dictionary.data.client.IDicClient
import ru.neosvet.dictionary.data.storage.DicStorage
import ru.neosvet.dictionary.entries.DicStrings
import ru.neosvet.dictionary.viewmodel.DictionaryViewModel
import ru.neosvet.dictionary.viewmodel.HistoryViewModel

object KoinModule {
    fun create(strings: DicStrings, storage: DicStorage) = module {
        val cicerone = Cicerone.create()
        single<NavigatorHolder> { cicerone.getNavigatorHolder() }
        single<Router> { cicerone.router }
        single<IDicClient> { DicClient.create() }
        single<IDictionarySource> {
            DictionarySource(
                client = get(),
                strings = strings
            )
        }
        viewModel {
            DictionaryViewModel(
                source = get(),
                storage = storage
            )
        }
        viewModel {
            HistoryViewModel(
                storage = storage
            )
        }
    }
}