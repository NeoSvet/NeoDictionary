package ru.neosvet.dictionary.view.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import ru.neosvet.dictionary.R
import ru.neosvet.dictionary.databinding.ItemWordBinding
import ru.neosvet.dictionary.entries.WordItem

class WordsAdapter(
    context: Context,
    private val onItemClickListener: ((WordItem, Event) -> Unit),
    private val filter: Filter
) : ArrayAdapter<String>(context, R.layout.item_word), Filterable {
    private val words = mutableListOf<WordItem>()

    enum class Event {
        OPEN, DELETE
    }

    inner class ViewHolder(private val vb: ItemWordBinding) : RecyclerView.ViewHolder(vb.root) {
        fun setItem(item: WordItem) = with(vb) {
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
        ItemWordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
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