package ru.neosvet.dictionary.data.client

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    fun create() = Retrofit.Builder()
        .baseUrl("https://api.dictionaryapi.dev/api/v2/entries/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(IClient::class.java)

    private val gson = GsonBuilder()
        .create()
}