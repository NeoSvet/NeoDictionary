package ru.neosvet.dictionary.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.neosvet.dictionary.databinding.ItemHistoryBinding
import ru.neosvet.dictionary.entries.WordItem
import ru.neosvet.dictionary.utils.ITimeFormatter

class HistoryAdapter(
    private val timeFormatter: ITimeFormatter,
    private val onItemClickListener: ((WordItem, Event) -> Unit)
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private val data = mutableListOf<WordItem>()

    enum class Event {
        OPEN, DELETE
    }

    inner class ViewHolder(
        private val binding: ItemHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: WordItem) {
            binding.run {
                tvWord.text = item.word
                if (item.time == 0L)
                    tvTime.visibility = View.GONE
                else
                    tvTime.text = timeFormatter.format(item.time)
                root.setOnClickListener {
                    onItemClickListener.invoke(item, Event.OPEN)
                }
                ivDelete.setOnClickListener {
                    onItemClickListener.invoke(item, Event.DELETE)
                }
            }
        }
    }

    fun setWords(words: List<WordItem>) {
        data.clear()
        data.addAll(words)
        notifyDataSetChanged()
    }

    fun removeWord(word: WordItem) {
        val i = data.indexOf(word)
        data.remove(word)
        notifyItemRemoved(i)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) =
        holder.setItem(data[position])

    override fun getItemCount() = data.size

}