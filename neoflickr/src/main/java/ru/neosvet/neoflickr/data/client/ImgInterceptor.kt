package ru.neosvet.neoflickr.data.client

import okhttp3.Interceptor
import okhttp3.Response

object ImgInterceptor : Interceptor {

    private const val CONST_PART = "&format=json&nojsoncallback=1&api_key=$FLICKR_API_KEY"

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request()
                .newBuilder()
                .url(chain.request().url().toString() + CONST_PART)
                .build()
        )

}