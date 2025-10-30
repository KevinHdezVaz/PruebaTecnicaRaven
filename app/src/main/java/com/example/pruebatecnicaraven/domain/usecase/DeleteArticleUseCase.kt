package com.example.pruebatecnicaraven.domain.usecase

import com.example.pruebatecnicaraven.data.repository.ArticleRepository
import javax.inject.Inject

class DeleteArticleUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(objectID: String) {
        repository.deleteArticle(objectID)
    }
}