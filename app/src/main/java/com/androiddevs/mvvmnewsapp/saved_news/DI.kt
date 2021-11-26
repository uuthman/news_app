package com.androiddevs.mvvmnewsapp.saved_news

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val savedNewsModule = module {
    viewModel {
        SavedNewsViewModel(get())
    }
}