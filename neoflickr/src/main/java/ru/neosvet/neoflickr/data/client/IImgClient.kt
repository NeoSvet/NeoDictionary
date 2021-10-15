package ru.neosvet.neoflickr.data.client

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.neosvet.neoflickr.entries.ImagesResponse

interface IImgClient {
    @GET("?method=flickr.photos.search")
    fun searchImages(
        @Query("text") query: String
    ): Single<ImagesResponse>
}