package ru.neosvet.dictionary.view.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import ru.neosvet.dictionary.R
import ru.neosvet.dictionary.entries.WordItem
import ru.neosvet.utils.viewById

class WordsAdapter(
    context: Context,
    private val onItemClickListener: ((WordItem, Event) -> Unit),
    private val filter: Filter
) : ArrayAdapter<String>(context, R.layout.item_word), Filterable {
    private val words = mutableListOf<WordItem>()

    enum class Event {
        OPEN, DELETE
    }

    inner class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        private val tvWord by root.viewById<TextView>(R.id.tv_word)
        private val ivDelete by root.viewById<ImageView>(R.id.iv_delete)

        fun setItem(item: WordItem) {
            tvWord.text = item.word
            tvWord.setOnClickListener {
                onItemClickListener.invoke(item, Event.OPEN)
            }
            ivDelete.setOnClickListener {
                onItemClickListener.invoke(item, Event.DELETE)
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
    ).apply {
        setItem(words[position])
    }.itemView

    override fun getItem(position: Int) = words[position].word

    override fun getCount() = words.size

    fun addWords(list: List<WordItem>) {
        words.clear()
        words.addAll(list)
        notifyDataSetChanged()
    }

    fun removeWord(item: WordItem) {
        words.remove(item)
        notifyDataSetChanged()
    }

    override fun getFilter() = filter
}