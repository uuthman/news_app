package com.androiddevs.mvvmnewsapp.breaking_news


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.util.Event
import com.androiddevs.mvvmnewsapp.util.asEvent
import kotlinx.coroutines.launch
import retrofit2.Response

class BreakingNewsViewModel(private val repository: NewsRepository) : ViewModel() {

    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    private val _uiState = MutableLiveData<BreakingNewsUiState>()
    val uiState : LiveData<BreakingNewsUiState> = _uiState

    private val _interaction = MutableLiveData<Event<BreakingNewsInteraction>>()
    val interaction : LiveData<Event<BreakingNewsInteraction>> = _interaction

    init {
        getBreakingNews("us")
    }

    fun navigateToArticleFragment(article: Article){
        val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article)
        _interaction.postValue(BreakingNewsInteraction.Navigate(action).asEvent())
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _uiState.postValue(BreakingNewsUiState.Loading)
        val response = repository.getBreakingNews(countryCode,breakingNewsPage)
        _uiState.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : BreakingNewsUiState {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if(breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                }else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return BreakingNewsUiState.Result(breakingNewsResponse ?: resultResponse)
            }
        }
        return BreakingNewsUiState.Error(response.message())
    }

}

sealed class BreakingNewsUiState{
    data class Result(val articles: NewsResponse) : BreakingNewsUiState()
    data class Error(val message: String) : BreakingNewsUiState()
    object Loading : BreakingNewsUiState()
}

sealed class BreakingNewsInteraction{
    data class Navigate(val destination: NavDirections) : BreakingNewsInteraction()
}