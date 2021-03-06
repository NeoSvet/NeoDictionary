package ru.neosvet.neoflickr.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import geekbrains.ru.utils.network.OnlineObserver
import ru.neosvet.neoflickr.R
import ru.neosvet.neoflickr.entries.ImagesState
import ru.neosvet.neoflickr.view.list.ImagesAdapter
import ru.neosvet.neoflickr.viewmodel.ImagesViewModel
import ru.neosvet.utils.viewById

class ImagesFragment : Fragment() {
    companion object {
        private const val ARG_QUERY = "query"
        fun newInstance(query: String) =
            ImagesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUERY, query)
                }
            }
    }

    private var errorBar: Snackbar? = null
    private val rvImages by viewById<RecyclerView>(R.id.rv_images)
    private val progressBar by viewById<ProgressBar>(R.id.progressBar)
    private val model: ImagesViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(ImagesViewModel::class.java)
    }
    private val onlineObserver: OnlineObserver by lazy {
        OnlineObserver(requireContext())
    }
    private val resultObserver =
        Observer<ImagesState.Model> { response ->
            when (response) {
                is ImagesState.Start -> errorBar?.dismiss()
                is ImagesState.Images -> onImages(response)
                is ImagesState.Error -> onError(response)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            it.getString(ARG_QUERY)?.let {
                model.search(it, onlineObserver)
            }
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

    private fun onImages(images: ImagesState.Images) {
        progressBar.visibility = View.GONE
        val adapter = ImagesAdapter(images.urls)
        rvImages.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun onError(result: ImagesState.Error) {
        progressBar.visibility = View.GONE
        val msg = getString(R.string.error) + ": " + result.error.message
        errorBar = Snackbar.make(
            rvImages, msg, Snackbar.LENGTH_INDEFINITE
        )
        errorBar?.show()
    }
}