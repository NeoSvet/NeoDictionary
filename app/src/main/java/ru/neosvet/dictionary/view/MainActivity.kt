package ru.neosvet.dictionary.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Filter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import ru.neosvet.dictionary.R
import ru.neosvet.dictionary.databinding.ActivityMainBinding
import ru.neosvet.dictionary.entries.*
import ru.neosvet.dictionary.view.list.MainAdapter
import ru.neosvet.dictionary.view.list.WordsAdapter
import ru.neosvet.dictionary.viewmodel.DictionaryViewModel

class MainActivity : AppCompatActivity() {
    private val model: DictionaryViewModel by inject()
    private lateinit var binding: ActivityMainBinding
    private val imm: InputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
    private val resultObserver = Observer<ModelResult> { result ->
        when (result.state) {
            StateResult.LIST -> onList(result as ListResult)
            StateResult.WORDS -> onWords(result as WordsResult)
            StateResult.ERROR -> onError(result as ErrorResult)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        model.result.observe(this, resultObserver)
    }

    override fun onPause() {
        model.result.removeObserver(resultObserver)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        menu?.findItem(R.id.search)?.actionView?.let {
            initSearchView(it)
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun initSearchView(view: View) {
        val searchView = view as SearchView
        searchView.queryHint = getString(R.string.query_hint)
        searchView.isSubmitButtonEnabled = true

        val searchAutoComplete =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        val searchManager: SearchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName)
        )
        adWords = WordsAdapter(
            context = this,
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

    private fun onList(result: ListResult) {
        errorBar?.dismiss()
        binding.tvWelcome.visibility = View.GONE
        val adapter = MainAdapter(
            onItemClickListener = onItemClickListener,
            data = result.list
        )
        binding.rvMain.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun onWords(result: WordsResult) {
        adWords.addWords(result.words.toMutableList())
    }

    private fun onError(result: ErrorResult) {
        var msg = result.error.message
        if (msg == null)
            msg = getString(R.string.unknown_error)
        else if (msg.contains("HTTP 404"))
            msg = getString(R.string.not_found)
        else
            msg = getString(R.string.error) + ": " + msg
        errorBar = Snackbar.make(
            binding.rvMain, msg, Snackbar.LENGTH_INDEFINITE
        )
        errorBar?.show()
    }
}