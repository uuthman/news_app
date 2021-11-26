package com.androiddevs.mvvmnewsapp.search_news

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchNewsModule = module {
    viewModel {
        SearchNewViewModel(get(),get())
    }
}