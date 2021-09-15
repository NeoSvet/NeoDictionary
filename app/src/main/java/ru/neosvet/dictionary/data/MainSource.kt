package ru.neosvet.dictionary.data

import io.reactivex.rxjava3.core.Single
import ru.neosvet.dictionary.data.client.IClient
import ru.neosvet.dictionary.entries.Definitions
import ru.neosvet.dictionary.entries.DicStrings
import ru.neosvet.dictionary.entries.ResultItem
import java.util.*

class MainSource(
    private val client: IClient,
    private val strings: DicStrings
) : DictionarySource {
    override fun searchWord(word: String, language: String): Single<List<ResultItem>> =
        client.searchWord(word, language)
            .map {
                val response = it[0]
                val list = mutableListOf<ResultItem>()
                list.add(ResultItem(strings.word + ": " + response.word, null, null))

                if (response.phonetics[0].text != null) {
                    list.add(ResultItem(strings.phonetics, null, null))
                    response.phonetics.forEach {
                        list.add(ResultItem(null, it.text, it.audio))
                    }
                }

                list.add(ResultItem(strings.meanings, null, null))
                response.meanings.forEach {
                    list.add(
                        ResultItem(
                            null,
                            strings.partOfSpeech + ": " + it.partOfSpeech,
                            null
                        )
                    )
                    it.definitions.forEach {
                        list.add(ResultItem(null, parseDefinition(it), null))
                        if (it.synonyms.isNotEmpty())
                            list.add(
                                ResultItem(
                                    null,
                                    strings.synonyms + ": " + listToString(it.synonyms),
                                    null
                                )
                            )
                        if (it.antonyms.isNotEmpty())
                            list.add(
                                ResultItem(
                                    null,
                                    strings.antonyms + ": " + listToString(it.antonyms),
                                    null
                                )
                            )
                    }
                }

                return@map list
            }

    private fun listToString(list: List<String>): String {
        val s = list.toString().substring(1)
        return s.substring(0, s.length - 1)
    }

    private fun parseDefinition(it: Definitions): String =
        strings.definition + ": " + it.definition + if (it.example != null)
            "\n" + strings.example + ": " + it.example else ""
}