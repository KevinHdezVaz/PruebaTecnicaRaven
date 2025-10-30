package com.example.pruebatecnicaraven.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @SerializedName("hits")
    val hits: List<ArticleDTO>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("nbHits")
    val nbHits: Int,
    @SerializedName("nbPages")
    val nbPages: Int
)
