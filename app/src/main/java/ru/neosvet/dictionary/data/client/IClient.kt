package ru.neosvet.dictionary.data.client

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import ru.neosvet.dictionary.entries.DictionaryResponse

interface IClient {
    @GET("{language}/{word}")
    fun searchWord(
        @Path("word") word: String,
        @Path("language") language: String
    ): Single<List<DictionaryResponse>>
}