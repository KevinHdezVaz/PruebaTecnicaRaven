package com.example.pruebatecnicaraven.domain.usecase

import com.example.pruebatecnicaraven.data.model.Article
import com.example.pruebatecnicaraven.data.repository.ArticleRepository
import com.example.pruebatecnicaraven.util.Resource
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetArticlesUseCaseTest {

    private lateinit var repository: ArticleRepository
    private lateinit var useCase: GetArticlesUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = GetArticlesUseCase(repository)
    }

    @Test
    fun `invoke should return flow from repository`() = runTest {

        val articles = listOf(
            Article("1", "Title", "Author", "Date", "URL", null, null, false)
        )
        val expectedFlow = flowOf(Resource.Success(articles))
        whenever(repository.getArticles()).thenReturn(expectedFlow)


        val result = useCase().toList()

        assertEquals(1, result.size)
        assertTrue(result[0] is Resource.Success)
        assertEquals(1, (result[0] as Resource.Success).data?.size)
    }

    @Test
    fun `invoke should return error from repository`() = runTest {

        val errorMessage = "Network error"
        val expectedFlow = flowOf(Resource.Error<List<Article>>(errorMessage))
        whenever(repository.getArticles()).thenReturn(expectedFlow)


        val result = useCase().toList()


        assertEquals(1, result.size)
        assertTrue(result[0] is Resource.Error)
        assertEquals(errorMessage, (result[0] as Resource.Error).message)
    }
}