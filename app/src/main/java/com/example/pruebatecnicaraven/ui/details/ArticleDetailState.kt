package com.example.pruebatecnicaraven.ui.detail

data class ArticleDetailState(
    val isLoading: Boolean = true,
    val url: String? = null,
    val title: String = "",
    val error: String? = null
)