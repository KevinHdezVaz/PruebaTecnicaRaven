package com.example.pruebatecnicaraven.domain.usecase

import com.example.pruebatecnicaraven.data.repository.ArticleRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteArticleUseCaseTest {

    private lateinit var repository: ArticleRepository
    private lateinit var useCase: DeleteArticleUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = DeleteArticleUseCase(repository)
    }

    @Test
    fun `invoke should call repository deleteArticle`() = runTest {

        val objectID = "123"


        useCase(objectID)


        verify(repository).deleteArticle(objectID)
    }
}