package ru.neosvet.app.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.neosvet.dictionary.R
import ru.neosvet.dictionary.entries.WordItem
import ru.neosvet.app.utils.ITimeFormatter
import ru.neosvet.utils.viewById

class HistoryAdapter(
    private val timeFormatter: ITimeFormatter,
    private val onItemClickListener: ((WordItem, Event) -> Unit)
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private val data = mutableListOf<WordItem>()

    enum class Event {
        OPEN, DELETE
    }

    inner class ViewHolder(
        private val root: View
    ) : RecyclerView.ViewHolder(root) {
        private val tvWord by root.viewById<TextView>(R.id.tv_word)
        private val tvTime by root.viewById<TextView>(R.id.tv_time)
        private val ivDelete by root.viewById<ImageView>(R.id.iv_delete)

        fun setItem(item: WordItem) {
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
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        )

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) =
        holder.setItem(data[position])

    override fun getItemCount() = data.size

}