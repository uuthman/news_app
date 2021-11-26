package com.androiddevs.mvvmnewsapp.breaking_news

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val breakingNewsModule = module {
    viewModel {
        BreakingNewsViewModel(get())
    }
}