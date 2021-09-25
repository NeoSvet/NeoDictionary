package ru.neosvet.dictionary.data.client

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object DicClient {
    const val baseUrl = "https://api.dictionaryapi.dev/api/v2/entries/"
    fun create() = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(IDicClient::class.java)

    private val gson = GsonBuilder()
        .create()
}