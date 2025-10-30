package com.example.pruebatecnicaraven.data.remote.api

import com.example.pruebatecnicaraven.data.remote.dto.ArticleResponse
import com.example.pruebatecnicaraven.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HackerNewsApi {

    @GET(Constants.SEARCH_ENDPOINT)
    suspend fun searchArticles(
        @Query("query") query: String = Constants.QUERY_PARAM
    ): Response<ArticleResponse>


}