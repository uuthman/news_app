package com.androiddevs.mvvmnewsapp.di

import androidx.room.Room
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ArticleDatabase::class.java,
            "article_db.db"
        ).build()
    }

    single {
        NewsRepository(
            db = get(),
            api = get()
        )
    }
}