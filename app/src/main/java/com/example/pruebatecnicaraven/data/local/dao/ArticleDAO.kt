package com.example.pruebatecnicaraven.data.local.dao

import androidx.room.*
import com.example.pruebatecnicaraven.data.local.entity.ArticleEntity
import com.example.pruebatecnicaraven.data.local.entity.DeletedArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {


    @Query("SELECT * FROM articles ORDER BY timestamp DESC")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE object_id = :objectID")
    suspend fun getArticleById(objectID: String): ArticleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntity)

    @Query("DELETE FROM articles WHERE object_id = :objectID")
    suspend fun deleteArticle(objectID: String)

    @Query("DELETE FROM articles")
    suspend fun deleteAllArticles()

     @Query("SELECT * FROM deleted_articles")
    fun getAllDeletedArticles(): Flow<List<DeletedArticleEntity>>

    @Query("SELECT object_id FROM deleted_articles")
    suspend fun getDeletedArticleIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeletedArticle(deletedArticle: DeletedArticleEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM deleted_articles WHERE object_id = :objectID)")
    suspend fun isArticleDeleted(objectID: String): Boolean
}