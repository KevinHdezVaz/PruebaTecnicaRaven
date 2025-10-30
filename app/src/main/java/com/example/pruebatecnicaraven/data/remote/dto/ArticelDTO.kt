package com.example.pruebatecnicaraven.data.remote.dto

import com.google.gson.annotations.SerializedName


data class ArticleDTO(
    @SerializedName("objectID")
    val objectID: String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("author")
    val author: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("story_title")
    val storyTitle: String?,
    @SerializedName("story_url")
    val storyUrl: String?
)