package com.example.pruebatecnicaraven.ui.articles.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.pruebatecnicaraven.data.model.Article
import com.example.pruebatecnicaraven.databinding.ItemArticleBinding
import com.example.pruebatecnicaraven.util.DateUtils


class ArticleViewHolder(
    private val binding: ItemArticleBinding,
    private val onItemClick: (Article) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article) {
        binding.apply {
            textViewTitle.text = article.getDisplayTitle()
            textViewAuthor.text = article.getDisplayAuthor()


            val timeAgo = DateUtils.getTimeAgo(article.createdAt)

            textViewDate.text = timeAgo

            root.setOnClickListener {
                onItemClick(article)
            }
        }
    }
}