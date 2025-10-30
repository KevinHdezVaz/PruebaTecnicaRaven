package com.example.pruebatecnicaraven.data.model

data class Article(
    val objectID: String,
    val title: String?,
    val author: String?,
    val createdAt: String?,
    val url: String?,
    val storyTitle: String?,
    val storyUrl: String?,
    val isDeleted: Boolean = false
) {

    fun getDisplayTitle(): String {
        return title ?: storyTitle ?: "Sin titulo"
    }


    fun getDisplayUrl(): String? {
        return url ?: storyUrl
    }


    fun getDisplayAuthor(): String {
        return author ?: "Desconocido"
    }
}