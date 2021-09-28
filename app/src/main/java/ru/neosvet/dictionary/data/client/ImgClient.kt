package ru.neosvet.dictionary.data.client

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ImgClient {
    const val BASE_URL = "https://api.flickr.com/services/rest/"
    fun create() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(ImgInterceptor)
                .build()
        )
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(IImgClient::class.java)

    private val gson = GsonBuilder()
        .create()
}