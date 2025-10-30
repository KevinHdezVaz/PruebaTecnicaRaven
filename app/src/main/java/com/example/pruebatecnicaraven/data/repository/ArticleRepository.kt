package com.example.pruebatecnicaraven.data.repository

import com.example.pruebatecnicaraven.data.model.Article
import com.example.pruebatecnicaraven.util.Resource
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    /**
     * Obtiene artículos desde la API o desde cache local si no hay internet
     * @return Flow que emite Resource con la lista de artículos
     */
    fun getArticles(): Flow<Resource<List<Article>>>

    /**
     * Refresca los artículos desde la API
     * @return Flow que emite Resource con la lista actualizada
     */
    fun refreshArticles(): Flow<Resource<List<Article>>>

    /**
     * Elimina un artículo de forma permanente (lo marca como eliminado)
     * @param objectID ID del artículo a eliminar
     */
    suspend fun deleteArticle(objectID: String)

    /**
     * Obtiene un artículo específico por su ID
     * @param objectID ID del artículo
     * @return El artículo o null si no existe
     */
    suspend fun getArticleById(objectID: String): Article?

    /**
     * Verifica si un artículo está marcado como eliminado
     * @param objectID ID del artículo
     * @return true si está eliminado, false en caso contrario
     */
    suspend fun isArticleDeleted(objectID: String): Boolean
}