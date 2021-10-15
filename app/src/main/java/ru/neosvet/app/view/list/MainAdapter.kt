package ru.neosvet.app.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.neosvet.dictionary.R
import ru.neosvet.dictionary.entries.ResultItem
import ru.neosvet.utils.viewById

class MainAdapter(
    private val onItemClickListener: ((ResultItem) -> Unit),
    private val data: List<ResultItem>
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    private val TYPE_TITLE = 0
    private val TYPE_LABEL = 1

    inner class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        private val tv by root.viewById<TextView>(R.id.tv_item)

        fun setItem(item: ResultItem) {
            if (item.title != null)
                tv.text = item.title
            else
                tv.text = item.description
            item.url?.let {
                itemView.setOnClickListener { onItemClickListener.invoke(item) }
            }
        }
    }

    override fun getItemViewType(position: Int) =
        if (data[position].title != null)
            TYPE_TITLE
        else
            TYPE_LABEL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_TITLE ->
                ViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_title, parent, false)
                )
            else -> //TYPE_LABEL
                ViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_label, parent, false)
                )
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.setItem(data[position])

    override fun getItemCount() = data.size
}