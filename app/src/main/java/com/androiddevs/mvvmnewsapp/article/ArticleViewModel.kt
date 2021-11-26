package com.androiddevs.mvvmnewsapp.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import kotlinx.coroutines.launch

class ArticleViewModel(private val repository: NewsRepository) : ViewModel(){

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }
}