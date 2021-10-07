package ru.neosvet.dictionary.view

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.core.scope.Scope
import ru.neosvet.dictionary.R
import ru.neosvet.dictionary.entries.HistoryState
import ru.neosvet.dictionary.entries.WordItem
import ru.neosvet.dictionary.utils.TimeFormatter
import ru.neosvet.dictionary.view.list.HistoryAdapter
import ru.neosvet.dictionary.view.screens.DictionaryScreen
import ru.neosvet.dictionary.viewmodel.HistoryViewModel
import ru.neosvet.utils.viewById

class HistoryFragment : Fragment() {
    private val scope: Scope = getKoin().createScope<HistoryFragment>()
    private val router: Router by inject()
    private val model: HistoryViewModel by scope.inject()
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
    private val rvHistory by viewById<RecyclerView>(R.id.rv_history)
    private val tvHistoryClear by viewById<TextView>(R.id.tv_history_clear)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val item = menu.add(R.string.clear)
        item.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear)
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        model.clearHistory()
        tvHistoryClear.visibility = View.VISIBLE
        return super.onOptionsItemSelected(item)
    }

    private fun openWord(item: WordItem) {
        router.newRootScreen(DictionaryScreen.create(item))
    }

    private fun deleteWord(item: WordItem) {
        model.deleteWord(item)
        adapter.removeWord(item)
    }

    private fun onWords(result: HistoryState.Words) {
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
        errorBar = Snackbar.make(
            rvHistory, msg, Snackbar.LENGTH_INDEFINITE
        )
        errorBar?.show()
    }

    override fun onDestroyView() {
        scope.close()
        super.onDestroyView()
    }
}