package com.example.strangernews.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.strangernews.R
import com.example.strangernews.data.model.Article
import com.example.strangernews.data.model.Article.Companion.DIFF_CALLBACK
import com.example.strangernews.databinding.LayoutAritcleMainItemBinding
import com.example.strangernews.ui.callback.ItemClickListener
import com.example.strangernews.utils.constant.Constants
import com.example.strangernews.utils.constant.Constants.ERROR_OCCUR
import com.example.strangernews.utils.extension.coverToDateTime
import com.example.strangernews.utils.extension.loadByUrl

class ArticleMainAdapter(private val listener: ItemClickListener<Article>) :
    ListAdapter<Article, ArticleMainAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutAritcleMainItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                item = this
                binding.apply{
                    imageArticleItem.loadByUrl(image)
                    txtArticleName.text = title
                    txtArticleDescription.text = description
                    txtArticlePublished.text = publishedAt.coverToDateTime(Constants.DATE_FORMAT_MMMM_YYYY_DD)
                    txtCountry.text = country
                    txtLanguage.text = language
                }
            }
        }
    }

    class ViewHolder(
        val binding: LayoutAritcleMainItemBinding,
        private val listener: ItemClickListener<Article>
    ) : RecyclerView.ViewHolder(binding.root) {

        var item: Article? = null

        init {
            itemView.setOnClickListener {
                listener.onItemClick(item)
            }
            itemView.setOnLongClickListener {
                listener.onItemLongClick(item)
            }
        }
    }
}
