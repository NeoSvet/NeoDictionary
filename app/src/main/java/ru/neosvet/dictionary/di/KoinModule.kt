package ru.neosvet.dictionary.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.neosvet.dictionary.data.DictionarySource
import ru.neosvet.dictionary.data.IDictionarySource
import ru.neosvet.dictionary.data.IImagesSource
import ru.neosvet.dictionary.data.ImagesSource
import ru.neosvet.dictionary.data.client.DicClient
import ru.neosvet.dictionary.data.client.IDicClient
import ru.neosvet.dictionary.data.client.IImgClient
import ru.neosvet.dictionary.data.client.ImgClient
import ru.neosvet.dictionary.data.storage.DicStorage
import ru.neosvet.dictionary.entries.DicStrings
import ru.neosvet.dictionary.viewmodel.DictionaryViewModel
import ru.neosvet.dictionary.viewmodel.HistoryViewModel
import ru.neosvet.dictionary.viewmodel.ImagesViewModel

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
        single<IImgClient> { ImgClient.create() }
        single<IImagesSource> {
            ImagesSource(
                client = get()
            )
        }
        viewModel {
            DictionaryViewModel(
                source = get(),
                storage = storage
            )
        }
        viewModel {
            ImagesViewModel(
                source = get()
            )
        }
        viewModel {
            HistoryViewModel(
                storage = storage
            )
        }
    }
}