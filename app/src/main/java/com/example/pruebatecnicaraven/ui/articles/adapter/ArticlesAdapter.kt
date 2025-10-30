package com.example.pruebatecnicaraven.ui.articles.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ListAdapter
import com.example.pruebatecnicaraven.R
import com.example.pruebatecnicaraven.data.model.Article
import com.example.pruebatecnicaraven.databinding.ItemArticleBinding


class ArticlesAdapter(
    private val onItemClick: (Article) -> Unit
) : ListAdapter<Article, ArticleViewHolder>(ArticleDiffCallback()) {

    private val animatedPositions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)

         if (!animatedPositions.contains(position)) {
            val animation = AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.item_animation_fall_down
            )
            holder.itemView.startAnimation(animation)
            animatedPositions.add(position)
        }
    }

    override fun onViewDetachedFromWindow(holder: ArticleViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun onCurrentListChanged(
        previousList: MutableList<Article>,
        currentList: MutableList<Article>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        animatedPositions.clear()
    }

}