package com.example.pruebatecnicaraven.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    @ColumnInfo(name = "object_id")
    val objectID: String,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: String?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "story_title")
    val storyTitle: String?,

    @ColumnInfo(name = "story_url")
    val storyUrl: String?,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)

