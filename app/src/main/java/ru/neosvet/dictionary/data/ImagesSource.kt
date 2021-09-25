package ru.neosvet.dictionary.data

import io.reactivex.rxjava3.core.Single
import ru.neosvet.dictionary.data.client.IImgClient
import ru.neosvet.dictionary.entries.Photo

class ImagesSource(
    private val client: IImgClient
) : IImagesSource {
    override fun search(query: String): Single<List<String>> =
        client.searchImages(query)
            .flatMap{
                if (it.stat == "fail")
                    Single.error(Exception(it.message))
                else {
                    val list = mutableListOf<String>()
                    it.photos?.photo?.forEach {
                        list.add(getUrl(it))
                    }
                    Single.just(list)
                }
            }

    private fun getUrl(photo: Photo) =
        "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_w.jpg"
    //see more https://www.flickr.com/services/api/misc.urls.html

}