package com.example.pruebatecnicaraven.ui.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebatecnicaraven.data.model.Article
import com.example.pruebatecnicaraven.domain.usecase.DeleteArticleUseCase
import com.example.pruebatecnicaraven.domain.usecase.GetArticlesUseCase
import com.example.pruebatecnicaraven.domain.usecase.RefreshArticlesUseCase
import com.example.pruebatecnicaraven.util.NetworkHelper
import com.example.pruebatecnicaraven.util.Resource
import com.example.pruebatecnicaraven.util.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val refreshArticlesUseCase: RefreshArticlesUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ArticlesState())
    val state: StateFlow<ArticlesState> = _state.asStateFlow()

    private var allArticles: List<Article> = emptyList()

    private val searchQuery = MutableStateFlow("")

    private val currentSortType = MutableStateFlow(SortType.RECENT)


    init {
        loadArticles()
        observeSearchAndSort()
    }

    private fun observeSearchAndSort() {
        viewModelScope.launch {
            combine(searchQuery, currentSortType) { query, sortType ->
                Pair(query, sortType)
            }.collect { (query, sortType) ->
                filterAndSortArticles(query, sortType)
            }
        }
    }

    fun loadArticles() {
        viewModelScope.launch {
            getArticlesUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        allArticles = result.data ?: emptyList()
                        filterAndSortArticles(searchQuery.value, currentSortType.value)
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = null,
                                isRefreshing = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message,
                                isRefreshing = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun refreshArticles() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            refreshArticlesUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        allArticles = result.data ?: emptyList()
                        filterAndSortArticles(searchQuery.value, currentSortType.value)
                        _state.update {
                            it.copy(
                                isRefreshing = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isRefreshing = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun deleteArticle(objectID: String) {
        viewModelScope.launch {
            try {
                deleteArticleUseCase(objectID)

                allArticles = allArticles.filter { it.objectID != objectID }
                filterAndSortArticles(searchQuery.value, currentSortType.value)
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Error al eliminar artículo: ${e.localizedMessage}")
                }
            }
        }
    }

    fun searchArticles(query: String) {
        searchQuery.value = query
    }

    fun sortArticles(sortType: SortType) {
        currentSortType.value = sortType
    }

    private fun filterAndSortArticles(query: String, sortType: SortType) {
        var filteredArticles = if (query.isBlank()) {
            allArticles
        } else {
            allArticles.filter { article ->
                article.getDisplayTitle().contains(query, ignoreCase = true) ||
                        article.getDisplayAuthor().contains(query, ignoreCase = true)
            }
        }

        filteredArticles = when (sortType) {
            SortType.RECENT -> {
                filteredArticles.sortedByDescending {
                    it.createdAt ?: ""
                }
            }
            SortType.OLDEST -> {
                filteredArticles.sortedBy {
                    it.createdAt ?: ""
                }
            }
            SortType.AUTHOR -> {
                filteredArticles.sortedBy {
                    it.getDisplayAuthor().lowercase()
                }
            }
        }

        _state.update { it.copy(articles = filteredArticles) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}