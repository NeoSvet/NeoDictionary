package ru.neosvet.neoflickr.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.LoadRequest
import ru.neosvet.neoflickr.R
import ru.neosvet.neoflickr.databinding.ItemImageBinding

class ImagesAdapter(
    private val urls: List<String>
) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        var pos: Int = -1

        fun setImage(url: String, position: Int) = with(binding) {
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
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setImage(urls[position], position)
    }

    override fun getItemCount() = urls.size
}