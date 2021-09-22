package ru.neosvet.dictionary.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.neosvet.dictionary.data.DictionarySource
import ru.neosvet.dictionary.data.IDictionarySource
import ru.neosvet.dictionary.data.client.Client
import ru.neosvet.dictionary.data.client.IClient
import ru.neosvet.dictionary.data.storage.DicStorage
import ru.neosvet.dictionary.entries.DicStrings
import ru.neosvet.dictionary.viewmodel.DictionaryViewModel

object KoinModule {
    fun create(strings: DicStrings, storage: DicStorage) = module {
        single<IClient> { Client.create() }
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
    }
}