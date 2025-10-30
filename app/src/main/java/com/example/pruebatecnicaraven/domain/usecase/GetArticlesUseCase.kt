package com.example.pruebatecnicaraven.domain.usecase

import com.example.pruebatecnicaraven.data.model.Article
import com.example.pruebatecnicaraven.data.repository.ArticleRepository
import com.example.pruebatecnicaraven.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(): Flow<Resource<List<Article>>> {
        return repository.getArticles()
    }
}