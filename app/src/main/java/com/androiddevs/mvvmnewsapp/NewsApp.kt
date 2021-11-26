package com.androiddevs.mvvmnewsapp

import android.app.Application
import com.androiddevs.mvvmnewsapp.article.articleModule
import com.androiddevs.mvvmnewsapp.breaking_news.breakingNewsModule
import com.androiddevs.mvvmnewsapp.di.apiModule
import com.androiddevs.mvvmnewsapp.di.dbModule
import com.androiddevs.mvvmnewsapp.saved_news.savedNewsModule
import com.androiddevs.mvvmnewsapp.search_news.searchNewsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApp : Application() {

    override fun onCreate() {
        super.onCreate()


        startKoin{
            androidContext(this@NewsApp)
            modules(
                apiModule,
                dbModule,
                breakingNewsModule,
                savedNewsModule,
                searchNewsModule,
                articleModule
            )
        }

    }
}