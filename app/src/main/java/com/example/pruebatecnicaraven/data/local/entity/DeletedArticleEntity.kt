package com.example.pruebatecnicaraven.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_articles")
data class DeletedArticleEntity(
    @PrimaryKey
    @ColumnInfo(name = "object_id")
    val objectID: String,

    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long = System.currentTimeMillis()
)