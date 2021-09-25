package ru.neosvet.dictionary.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.neosvet.dictionary.R
import ru.neosvet.dictionary.databinding.ActivityMainBinding
import ru.neosvet.dictionary.entries.*
import ru.neosvet.dictionary.view.list.MainAdapter
import ru.neosvet.dictionary.viewmodel.DictionaryViewModel
import ru.neosvet.dictionary.viewmodel.IDictionaryViewModel

class MainActivity : AppCompatActivity() {
    private val model: IDictionaryViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DictionaryViewModel::class.java)
    }
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
    private val resultObserver = Observer<ModelResult> { result ->
        when (result.state) {
            StateResult.LIST -> onList(result as ListResult)
            StateResult.ERROR -> onError(result as ErrorResult)
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

        val search = menu?.findItem(R.id.search)?.actionView as SearchView
        search.queryHint = getString(R.string.query_hint)
        search.isSubmitButtonEnabled = true

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                model.searchWord(query, detectLanguage())
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        model.word?.let {
            search.setQuery(it, false)
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun detectLanguage(): String {
        val lang = imm.currentInputMethodSubtype.locale
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