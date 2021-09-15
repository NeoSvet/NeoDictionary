package ru.neosvet.dictionary.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.neosvet.dictionary.R
import ru.neosvet.dictionary.databinding.ItemLabelBinding
import ru.neosvet.dictionary.databinding.ItemTitleBinding
import ru.neosvet.dictionary.entries.ResultItem
import ru.neosvet.dictionary.presenter.IListPresenter

class MainAdapter(
    private val presenter: IListPresenter
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    private val TYPE_TITLE = 0
    private val TYPE_LABEL = 1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ListView {
        override var pos = -1

        override fun setItem(item: ResultItem) {
            val tv = itemView.findViewById(R.id.tv_item) as TextView
            if (item.title != null)
                tv.text = item.title
            else
                tv.text = item.description
        }
    }

    override fun getItemViewType(position: Int) =
        if (presenter.isTitle(position))
            TYPE_TITLE
        else
            TYPE_LABEL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_TITLE ->
                ViewHolder(
                    ItemTitleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ).root
                )
            else -> //TYPE_LABEL
                ViewHolder(
                    ItemLabelBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ).root
                ).apply {
                    itemView.setOnClickListener { presenter.itemClickListener?.invoke(this) }
                }
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        presenter.bindView(holder.apply { pos = position })

    override fun getItemCount() = presenter.getCount()
}