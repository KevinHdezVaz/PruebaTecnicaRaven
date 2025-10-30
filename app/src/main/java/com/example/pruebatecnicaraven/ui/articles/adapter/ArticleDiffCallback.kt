package com.example.pruebatecnicaraven.ui.articles.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.pruebatecnicaraven.data.model.Article


class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {


    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.objectID == newItem.objectID
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}