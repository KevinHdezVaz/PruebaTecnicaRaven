package com.example.pruebatecnicaraven.domain.mapper

import com.example.pruebatecnicaraven.data.model.Article
import com.example.pruebatecnicaraven.data.remote.dto.ArticleDTO

object ArticleMapper {

    fun fromDTO(dto: ArticleDTO): Article {
        return Article(
            objectID = dto.objectID,
            title = dto.title,
            author = dto.author,
            createdAt = dto.createdAt,
            url = dto.url,
            storyTitle = dto.storyTitle,
            storyUrl = dto.storyUrl,
            isDeleted = false
        )
    }

    fun fromDTOList(dtoList: List<ArticleDTO>): List<Article> {
        return dtoList.map { fromDTO(it) }
    }
}