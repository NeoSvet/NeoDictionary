package ru.neosvet.neoflickr.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.neosvet.neoflickr.R
import ru.neosvet.neoflickr.databinding.FragmentImagesBinding
import ru.neosvet.neoflickr.entries.ImagesState
import ru.neosvet.neoflickr.view.list.ImagesAdapter
import ru.neosvet.neoflickr.viewmodel.ImagesViewModel

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

    private var binding: FragmentImagesBinding? = null
    private var errorBar: Snackbar? = null
    private val model: ImagesViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(ImagesViewModel::class.java)
    }
    private val resultObserver =
        Observer<ImagesState.Model> { result ->
            when (result.state) {
                ImagesState.State.IMAGES -> onImages(result as ImagesState.Images)
                ImagesState.State.ERROR -> onError(result as ImagesState.Error)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentImagesBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            it.getString(ARG_QUERY)?.let {
                model.search(it)
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

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun onImages(images: ImagesState.Images) = binding?.run {
        progressBar.visibility = View.GONE
        val adapter = ImagesAdapter(images.urls)
        rvImages.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun onError(result: ImagesState.Error) = binding?.run {
        progressBar.visibility = View.GONE
        val msg = getString(R.string.error) + ": " + result.error.message
        errorBar = Snackbar.make(
            rvImages, msg, Snackbar.LENGTH_INDEFINITE
        )
        errorBar?.show()
    }
}