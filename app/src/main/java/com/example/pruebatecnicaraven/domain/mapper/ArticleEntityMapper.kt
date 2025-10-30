package com.example.pruebatecnicaraven.domain.mapper

import com.example.pruebatecnicaraven.data.local.entity.ArticleEntity
import com.example.pruebatecnicaraven.data.model.Article

object ArticleEntityMapper {

    fun toEntity(article: Article): ArticleEntity {
        return ArticleEntity(
            objectID = article.objectID,
            title = article.title,
            author = article.author,
            createdAt = article.createdAt,
            url = article.url,
            storyTitle = article.storyTitle,
            storyUrl = article.storyUrl
        )
    }

    fun fromEntity(entity: ArticleEntity): Article {
        return Article(
            objectID = entity.objectID,
            title = entity.title,
            author = entity.author,
            createdAt = entity.createdAt,
            url = entity.url,
            storyTitle = entity.storyTitle,
            storyUrl = entity.storyUrl,
            isDeleted = false
        )
    }

    fun toEntityList(articles: List<Article>): List<ArticleEntity> {
        return articles.map { toEntity(it) }
    }

    fun fromEntityList(entities: List<ArticleEntity>): List<Article> {
        return entities.map { fromEntity(it) }
    }
}