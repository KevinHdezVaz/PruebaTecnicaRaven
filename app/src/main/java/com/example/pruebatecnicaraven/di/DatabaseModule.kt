package com.example.pruebatecnicaraven.di

import android.content.Context
import androidx.room.Room
import com.example.pruebatecnicaraven.data.local.dao.ArticleDao
import com.example.pruebatecnicaraven.data.local.database.ArticleDatabase
import com.example.pruebatecnicaraven.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideArticleDatabase(
        @ApplicationContext context: Context
    ): ArticleDatabase {
        return Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(database: ArticleDatabase): ArticleDao {
        return database.articleDao()
    }
}