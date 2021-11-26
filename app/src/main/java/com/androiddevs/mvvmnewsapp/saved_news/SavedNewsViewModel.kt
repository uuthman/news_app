package com.androiddevs.mvvmnewsapp.saved_news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.util.Event
import com.androiddevs.mvvmnewsapp.util.asEvent
import kotlinx.coroutines.launch

class SavedNewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _uiState = MutableLiveData<SavedNewsUiState>()
    val uiState : LiveData<SavedNewsUiState> = _uiState

    private val _interaction = MutableLiveData<Event<SavedNewsInteraction>>()
    val interaction : LiveData<Event<SavedNewsInteraction>> = _interaction

    init {
       getSavedNews()
    }


    fun navigateToArticleFragment(article: Article){
        val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
        _interaction.postValue(SavedNewsInteraction.Navigate(action).asEvent())
    }

    private fun getSavedNews(){
        _uiState.postValue(SavedNewsUiState.Result(repository.getSavedNews()))
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

}

sealed class SavedNewsInteraction{
    data class Navigate(val destination: NavDirections) : SavedNewsInteraction()
}

sealed class SavedNewsUiState{
    data class Result(val articles: LiveData<List<Article>>) : SavedNewsUiState()
    data class Error(val message: String) : SavedNewsUiState()
}