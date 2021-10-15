package ru.neosvet.dictionary.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import geekbrains.ru.utils.network.OnlineObserver
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.neosvet.dictionary.R
import ru.neosvet.dictionary.data.DictionarySource
import ru.neosvet.dictionary.data.IDictionarySource
import ru.neosvet.dictionary.data.client.DicClient
import ru.neosvet.dictionary.data.client.IDicClient
import ru.neosvet.dictionary.data.storage.DicStorage
import ru.neosvet.dictionary.entries.DicStrings
import ru.neosvet.dictionary.view.DictionaryFragment
import ru.neosvet.dictionary.view.HistoryFragment
import ru.neosvet.dictionary.viewmodel.DictionaryViewModel
import ru.neosvet.dictionary.viewmodel.HistoryViewModel

object KoinModule {
    fun create(context: Context) = module {
        val storage: DicStorage = DicStorage.get(context)
        val cicerone = Cicerone.create()
        single<NavigatorHolder> { cicerone.getNavigatorHolder() }
        single<Router> { cicerone.router }
        single<OnlineObserver> { OnlineObserver(context) }

        scope(named<DictionaryFragment>()) {
            val strings: DicStrings = with(context) {
                DicStrings(
                    word = getString(R.string.word),
                    phonetics = getString(R.string.phonetics),
                    meanings = getString(R.string.meanings),
                    partOfSpeech = getString(R.string.partOfSpeech),
                    definition = getString(R.string.definition),
                    example = getString(R.string.example),
                    synonyms = getString(R.string.synonyms),
                    antonyms = getString(R.string.antonyms)
                )
            }

            scoped<IDicClient> { DicClient.create() }
            scoped<IDictionarySource> {
                DictionarySource(
                    client = get(),
                    strings = strings
                )
            }
            viewModel {
                DictionaryViewModel(
                    observer = get(),
                    source = get(),
                    storage = storage
                )
            }
        }

        scope(named<HistoryFragment>()) {
            viewModel {
                HistoryViewModel(
                    storage = storage
                )
            }
        }
    }
}