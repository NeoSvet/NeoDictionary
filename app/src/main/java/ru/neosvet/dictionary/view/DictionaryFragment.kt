package ru.neosvet.dictionary.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Filter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import ru.neosvet.dictionary.R
import ru.neosvet.dictionary.entries.DictionaryState
import ru.neosvet.dictionary.entries.ResultItem
import ru.neosvet.dictionary.entries.WordItem
import ru.neosvet.dictionary.view.list.MainAdapter
import ru.neosvet.dictionary.view.list.WordsAdapter
import ru.neosvet.dictionary.viewmodel.DictionaryViewModel
import ru.neosvet.dictionary.databinding.FragmentDictionaryBinding

class DictionaryFragment : Fragment() {
    companion object {
        private const val ARG_WORD_ID = "wordId"
        fun newInstance(wordId: Int = 0) =
            DictionaryFragment().apply {
                if (wordId > 0)
                    arguments = Bundle().apply {
                        putInt(ARG_WORD_ID, wordId)
                    }
            }
    }

    private var binding: FragmentDictionaryBinding? = null
    private val model: DictionaryViewModel by inject()
    private val imm: InputMethodManager by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
    private var errorBar: Snackbar? = null
    private val onItemClickListener: ((ResultItem) -> Unit) = { item ->
        item.url?.let {
            if (it.indexOf("http") == 0)
                openUrl(it)
            else
                openUrl("http:$it")
        }
    }
    private val onWordClickListener: ((WordItem, WordsAdapter.Event) -> Unit) = { word, event ->
        when (event) {
            WordsAdapter.Event.OPEN -> model.openWord(word.id)
            WordsAdapter.Event.DELETE -> {
                adWords.removeWord(word)
                model.deleteWord(word.id)
            }
        }
    }
    private val resultObserver = Observer<DictionaryState.Model> { result ->
        when (result.state) {
            DictionaryState.State.RESULTS -> onResults(result as DictionaryState.Results)
            DictionaryState.State.WORDS -> onWords(result as DictionaryState.Words)
            DictionaryState.State.ERROR -> onError(result as DictionaryState.Error)
        }
    }
    private lateinit var adWords: WordsAdapter
    private val wordsFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            if (constraint.isNullOrEmpty()) return results
            val s = constraint.toString().lowercase()
            model.getWords(s)
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDictionaryBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            val id = it.getInt(ARG_WORD_ID)
            if (id > 0)
                model.openWord(id)
        }
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
        inflater.inflate(R.menu.main, menu)
        initSearchView(menu.findItem(R.id.search).actionView)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initSearchView(view: View) {
        val searchView = view as SearchView
        searchView.queryHint = getString(R.string.query_hint)
        searchView.isSubmitButtonEnabled = true

        val searchAutoComplete =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        val searchManager: SearchManager =
            requireContext().getSystemService(AppCompatActivity.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(requireActivity().componentName)
        )
        adWords = WordsAdapter(
            context = requireContext(),
            onItemClickListener = onWordClickListener,
            filter = wordsFilter
        )
        searchAutoComplete.setAdapter(adWords)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                model.searchWord(query, detectLanguage())
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        model.word?.let {
            searchView.setQuery(it, false)
        }
    }

    private fun detectLanguage(): String {
        val type = imm.currentInputMethodSubtype
        var lang: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lang = type.languageTag
            if (lang.isEmpty())
                lang = type.locale
        } else
            lang = type.locale
        if (lang.length > 2)
            lang = lang.substring(0, 2)
        if (lang.isEmpty() || lang == "zz")
            return "en"
        return lang
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onResults(result: DictionaryState.Results) {
        errorBar?.dismiss()
        val adapter = MainAdapter(
            onItemClickListener = onItemClickListener,
            data = result.list
        )
        binding?.run {
            tvWelcome.visibility = View.GONE
            rvMain.adapter = adapter
        }
        adapter.notifyDataSetChanged()
    }

    private fun onWords(result: DictionaryState.Words) {
        adWords.addWords(result.words.toMutableList())
    }

    private fun onError(result: DictionaryState.Error) {
        var msg = result.error.message
        if (msg == null)
            msg = getString(R.string.unknown_error)
        else if (msg.contains("HTTP 404"))
            msg = getString(R.string.not_found)
        else
            msg = getString(R.string.error) + ": " + msg
        binding?.run {
            errorBar = Snackbar.make(
                rvMain, msg, Snackbar.LENGTH_INDEFINITE
            )
            errorBar?.show()
        }
    }
}