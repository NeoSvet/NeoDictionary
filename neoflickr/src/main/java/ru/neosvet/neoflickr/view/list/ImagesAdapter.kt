package ru.neosvet.neoflickr.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.LoadRequest
import ru.neosvet.neoflickr.R
import ru.neosvet.utils.viewById

class ImagesAdapter(
    private val urls: List<String>
) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    inner class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        private val ivImage by root.viewById<ImageView>(R.id.iv_image)
        var pos: Int = -1

        fun setImage(url: String, position: Int) {
            pos = position
            val context = ivImage.context

            val request = LoadRequest.Builder(context)
                .data(url)
                .target(
                    onStart = {
                        ivImage.setImageResource(R.drawable.load_image)
                    },
                    onSuccess = { result ->
                        ivImage.setImageDrawable(result)
                    },
                    onError = {
                        ivImage.setImageResource(R.drawable.no_image)
                    }
                )
                .build()

            ImageLoader(context).execute(request)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setImage(urls[position], position)
    }

    override fun getItemCount() = urls.size
}