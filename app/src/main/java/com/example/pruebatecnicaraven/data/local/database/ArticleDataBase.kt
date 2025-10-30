package com.example.pruebatecnicaraven.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pruebatecnicaraven.data.local.dao.ArticleDao
import com.example.pruebatecnicaraven.data.local.entity.ArticleEntity
import com.example.pruebatecnicaraven.data.local.entity.DeletedArticleEntity

@Database(
    entities = [ArticleEntity::class, DeletedArticleEntity::class],
    version = 1
)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}