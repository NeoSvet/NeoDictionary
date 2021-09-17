package ru.neosvet.dictionary.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.neosvet.dictionary.App
import ru.neosvet.dictionary.R
import ru.neosvet.dictionary.presenter.DictionaryPresenter
import ru.neosvet.dictionary.presenter.IListPresenter
import ru.neosvet.dictionary.view.list.MainAdapter

class MainActivity : AppCompatActivity(), DictionaryView {
    private val presenter: DictionaryPresenter by lazy {
        App.instance.dictionary
    }
    private val rvMain: RecyclerView by lazy {
        findViewById(R.id.rv_main)
    }
    private val tvWelcome: TextView by lazy {
        findViewById(R.id.tv_welcome)
    }
    private val imm: InputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
    private var errorBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val search = menu?.findItem(R.id.search)?.actionView as SearchView
        search.queryHint = getString(R.string.query_hint)
        search.isSubmitButtonEnabled = true

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.searchWord(query, detectLanguage())
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        presenter.word?.let {
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

    override fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun updateList(list: IListPresenter) {
        errorBar?.dismiss()
        tvWelcome.visibility = View.GONE
        val adapter = MainAdapter(list)
        rvMain.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onError(t: Throwable) {
        var msg = t.message
        if (msg == null)
            msg = getString(R.string.unknown_error)
        else if (msg.contains("HTTP 404"))
            msg = getString(R.string.not_found)
        else
            msg = getString(R.string.error) + ": " + msg
        errorBar = Snackbar.make(
            rvMain, msg, Snackbar.LENGTH_INDEFINITE
        )
        errorBar?.show()
    }
}