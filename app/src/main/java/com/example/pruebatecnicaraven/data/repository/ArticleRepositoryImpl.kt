package com.example.pruebatecnicaraven.data.repository

import com.example.pruebatecnicaraven.data.local.dao.ArticleDao
import com.example.pruebatecnicaraven.data.local.entity.DeletedArticleEntity
import com.example.pruebatecnicaraven.data.model.Article
import com.example.pruebatecnicaraven.data.remote.api.HackerNewsApi
import com.example.pruebatecnicaraven.domain.mapper.ArticleEntityMapper
import com.example.pruebatecnicaraven.domain.mapper.ArticleMapper
import com.example.pruebatecnicaraven.util.Constants
import com.example.pruebatecnicaraven.util.NetworkHelper
import com.example.pruebatecnicaraven.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepositoryImpl @Inject constructor(
    private val api: HackerNewsApi,
    private val dao: ArticleDao,
    private val networkHelper: NetworkHelper
) : ArticleRepository {

    override fun getArticles(): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())

        try {
            val cachedArticles = dao.getAllArticles().first()
            val deletedIds = dao.getDeletedArticleIds()

            val filteredCachedArticles = cachedArticles
                .filter { it.objectID !in deletedIds }
                .let { ArticleEntityMapper.fromEntityList(it) }

            if (filteredCachedArticles.isNotEmpty()) {
                emit(Resource.Success(filteredCachedArticles))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (networkHelper.isNetworkConnected()) {
            try {
                val response = api.searchArticles(query = Constants.QUERY_PARAM)

                if (response.isSuccessful && response.body() != null) {
                    val articleDTOs = response.body()!!.hits
                    val articles = ArticleMapper.fromDTOList(articleDTOs)

                    val deletedIds = dao.getDeletedArticleIds()

                    val filteredArticles = articles.filter { it.objectID !in deletedIds }

                    val entitiesToSave = ArticleEntityMapper.toEntityList(filteredArticles)
                    dao.deleteAllArticles()
                    dao.insertArticles(entitiesToSave)

                    emit(Resource.Success(filteredArticles))
                } else {
                    emit(Resource.Error(
                        message = "Error del servidor: ${response.code()}",
                        data = null
                    ))
                }
            } catch (e: HttpException) {
                emit(Resource.Error(
                    message = "Error de red: ${e.localizedMessage}",
                    data = null
                ))
            } catch (e: IOException) {
                emit(Resource.Error(
                    message = "No se pudo conectar al servidor. Verifica tu conexión.",
                    data = null
                ))
            } catch (e: Exception) {
                emit(Resource.Error(
                    message = "Error inesperado: ${e.localizedMessage}",
                    data = null
                ))
            }
        } else {
            val cachedArticles = dao.getAllArticles().first()
            if (cachedArticles.isEmpty()) {
                emit(Resource.Error(
                    message = "Sin conexión a internet y no hay datos guardados",
                    data = emptyList()
                ))
            }
        }
    }

    override fun refreshArticles(): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())

        // Verificar conexión a internet
        if (!networkHelper.isNetworkConnected()) {
            emit(Resource.Error(
                message = "Sin conexión a internet",
                data = null
            ))
            return@flow
        }

        try {

            val response = api.searchArticles(query = Constants.QUERY_PARAM)

            if (response.isSuccessful && response.body() != null) {
                val articleDTOs = response.body()!!.hits
                val articles = ArticleMapper.fromDTOList(articleDTOs)

                val deletedIds = dao.getDeletedArticleIds()

                val filteredArticles = articles.filter { it.objectID !in deletedIds }

                val entitiesToSave = ArticleEntityMapper.toEntityList(filteredArticles)
                dao.deleteAllArticles()
                dao.insertArticles(entitiesToSave)

                emit(Resource.Success(filteredArticles))
            } else {
                emit(Resource.Error(
                    message = "Error al refrescar: ${response.code()}",
                    data = null
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "Error de red: ${e.localizedMessage}",
                data = null
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "Error de conexión: ${e.localizedMessage}",
                data = null
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = "Error inesperado: ${e.localizedMessage}",
                data = null
            ))
        }
    }

    override suspend fun deleteArticle(objectID: String) {
        try {
            //eliminado permanente
            val deletedArticle = DeletedArticleEntity(objectID = objectID)
            dao.insertDeletedArticle(deletedArticle)


            //Eliminar de tabla
            dao.deleteArticle(objectID)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getArticleById(objectID: String): Article? {
        return try {
            val entity = dao.getArticleById(objectID)
            entity?.let { ArticleEntityMapper.fromEntity(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun isArticleDeleted(objectID: String): Boolean {
        return try {
            dao.isArticleDeleted(objectID)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}