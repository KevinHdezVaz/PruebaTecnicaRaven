package com.example.pruebatecnicaraven.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ArticleDetailState())
    val state: StateFlow<ArticleDetailState> = _state.asStateFlow()

    init {
        val url = savedStateHandle.get<String>("articleUrl")
        val title = savedStateHandle.get<String>("articleTitle") ?: "Article"

        _state.update {
            it.copy(
                url = url,
                title = title,
                isLoading = false
            )
        }
    }

    fun onPageStarted() {
        _state.update { it.copy(isLoading = true) }
    }

    fun onPageFinished() {
        _state.update { it.copy(isLoading = false) }
    }

    fun onError(error: String) {
        _state.update { it.copy(error = error, isLoading = false) }
    }
}