package com.example.pruebatecnicaraven.ui.articles

import com.example.pruebatecnicaraven.data.model.Article

data class ArticlesState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
)