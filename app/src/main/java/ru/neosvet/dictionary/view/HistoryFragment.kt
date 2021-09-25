package ru.neosvet.dictionary.view

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import ru.neosvet.dictionary.R
import ru.neosvet.dictionary.databinding.FragmentHistoryBinding
import ru.neosvet.dictionary.entries.HistoryState
import ru.neosvet.dictionary.entries.WordItem
import ru.neosvet.dictionary.utils.TimeFormatter
import ru.neosvet.dictionary.view.list.HistoryAdapter
import ru.neosvet.dictionary.view.screens.DictionaryScreen
import ru.neosvet.dictionary.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {
    private var binding: FragmentHistoryBinding? = null
    private val router: Router by inject()
    private val model: HistoryViewModel by inject()
    private val resultObserver = Observer<HistoryState.Model> { result ->
        when (result.state) {
            HistoryState.State.WORDS -> onWords(result as HistoryState.Words)
            HistoryState.State.ERROR -> onError(result as HistoryState.Error)
        }
    }
    private var errorBar: Snackbar? = null
    private val adapter: HistoryAdapter by lazy {
        HistoryAdapter(
            TimeFormatter(resources.getStringArray(R.array.time))
        ) { item, event ->
            when (event) {
                HistoryAdapter.Event.OPEN -> openWord(item)
                HistoryAdapter.Event.DELETE -> deleteWord(item)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHistoryBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        model.getWords()
    }

    override fun onResume() {
        super.onResume()
        model.result.observe(this, resultObserver)
    }

    override fun onPause() {
        model.result.removeObserver(resultObserver)
        super.onPause()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val item = menu.add(R.string.clear)
        item.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear)
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        model.clearHistory()
        binding?.tvHistoryClear?.visibility = View.VISIBLE
        return super.onOptionsItemSelected(item)
    }

    private fun openWord(item: WordItem) {
        router.exit()
        router.replaceScreen(DictionaryScreen.create(item))
    }

    private fun deleteWord(item: WordItem) {
        model.deleteWord(item)
        adapter.removeWord(item)
    }

    private fun onWords(result: HistoryState.Words) = binding?.run {
        tvHistoryClear.visibility = View.GONE
        adapter.setWords(result.words)
        rvHistory.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun onError(result: HistoryState.Error) {
        var msg = result.error.message
        if (msg == null)
            msg = getString(R.string.unknown_error)
        else if (msg.contains("HTTP 404"))
            msg = getString(R.string.not_found)
        else
            msg = getString(R.string.error) + ": " + msg
        binding?.run {
            errorBar = Snackbar.make(
                rvHistory, msg, Snackbar.LENGTH_INDEFINITE
            )
            errorBar?.show()
        }
    }
}