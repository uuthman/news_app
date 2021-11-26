package com.androiddevs.mvvmnewsapp.saved_news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.databinding.FragmentSavedNewsBinding
import com.androiddevs.mvvmnewsapp.extenstions.viewBinding
import com.androiddevs.mvvmnewsapp.util.observeEvent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {


    private val binding by viewBinding(FragmentSavedNewsBinding::bind)
    private val viewModel: SavedNewsViewModel by viewModel()

    private val newsAdapter = NewsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnClickListener()
        setupObservers()
        setupAdapter()
        setupTouchHelper()
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val article = newsAdapter.differ.currentList[position]
            viewModel.deleteArticle(article)
            view?.let {
                Snackbar.make(it,"Successfully deleted article",Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                }.show()
            }
        }
    }

    private fun setupTouchHelper(){
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedNews)
        }
    }

    private fun setupOnClickListener(){
        newsAdapter.setOnItemClickListener {
            viewModel.navigateToArticleFragment(it)
        }
    }

    private fun setupAdapter(){
        binding.apply {
            rvSavedNews.adapter = newsAdapter
        }
    }

    private fun setupObservers(){
        viewModel.interaction.observeEvent(viewLifecycleOwner){
            when(it){
                is SavedNewsInteraction.Navigate -> {
                    findNavController().navigate(
                        it.destination
                    )
                }
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner){
            when(it){
                is SavedNewsUiState.Error -> {

                }
                is SavedNewsUiState.Result -> {
                    it.articles.observe(viewLifecycleOwner, Observer { articles -> newsAdapter.differ.submitList(articles) })
                }
            }
        }
    }
}