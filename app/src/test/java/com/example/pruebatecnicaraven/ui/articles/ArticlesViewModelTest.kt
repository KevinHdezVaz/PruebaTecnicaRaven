package com.example.pruebatecnicaraven.ui.articles

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.pruebatecnicaraven.data.model.Article
import com.example.pruebatecnicaraven.domain.usecase.DeleteArticleUseCase
import com.example.pruebatecnicaraven.domain.usecase.GetArticlesUseCase
import com.example.pruebatecnicaraven.domain.usecase.RefreshArticlesUseCase
import com.example.pruebatecnicaraven.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getArticlesUseCase: GetArticlesUseCase
    private lateinit var refreshArticlesUseCase: RefreshArticlesUseCase
    private lateinit var deleteArticleUseCase: DeleteArticleUseCase
    private lateinit var viewModel: ArticlesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getArticlesUseCase = mock()
        refreshArticlesUseCase = mock()
        deleteArticleUseCase = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be empty`() = runTest {

        whenever(getArticlesUseCase()).thenReturn(flowOf(Resource.Loading()))


        viewModel = ArticlesViewModel(
            getArticlesUseCase,
            refreshArticlesUseCase,
            deleteArticleUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()


        val state = viewModel.state.value
        assertTrue(state.articles.isEmpty())
    }

    @Test
    fun `loadArticles should update state with success`() = runTest {

        val articles = listOf(
            Article("1", "Title", "Author", "Date", "URL", null, null, false)
        )
        whenever(getArticlesUseCase()).thenReturn(
            flowOf(Resource.Success(articles))
        )


        viewModel = ArticlesViewModel(
            getArticlesUseCase,
            refreshArticlesUseCase,
            deleteArticleUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()


        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(1, state.articles.size)
        assertNull(state.error)
    }

    @Test
    fun `loadArticles should update state with error`() = runTest {

        val errorMessage = "Network error"
        whenever(getArticlesUseCase()).thenReturn(
            flowOf(Resource.Error(errorMessage))
        )


        viewModel = ArticlesViewModel(
            getArticlesUseCase,
            refreshArticlesUseCase,
            deleteArticleUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()


        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `deleteArticle should call useCase and remove from list`() = runTest {

        val articles = listOf(
            Article("1", "Title1", "Author", "Date", "URL", null, null, false),
            Article("2", "Title2", "Author", "Date", "URL", null, null, false)
        )
        whenever(getArticlesUseCase()).thenReturn(flowOf(Resource.Success(articles)))

        viewModel = ArticlesViewModel(
            getArticlesUseCase,
            refreshArticlesUseCase,
            deleteArticleUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()


        viewModel.deleteArticle("1")
        testDispatcher.scheduler.advanceUntilIdle()


        verify(deleteArticleUseCase).invoke("1")
        val state = viewModel.state.value
        assertEquals(1, state.articles.size)
        assertEquals("2", state.articles[0].objectID)
    }

    @Test
    fun `clearError should set error to null`() = runTest {

        whenever(getArticlesUseCase()).thenReturn(
            flowOf(Resource.Error("Error"))
        )
        viewModel = ArticlesViewModel(
            getArticlesUseCase,
            refreshArticlesUseCase,
            deleteArticleUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()


        viewModel.clearError()


        assertNull(viewModel.state.value.error)
    }
}