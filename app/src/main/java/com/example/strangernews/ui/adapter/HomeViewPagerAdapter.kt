package com.example.strangernews.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.strangernews.R
import com.example.strangernews.data.model.Article
import com.example.strangernews.data.model.Article.Companion.DIFF_CALLBACK
import com.example.strangernews.databinding.LayoutHomeViewpagerItemBinding
import com.example.strangernews.ui.callback.ItemClickListener
import com.example.strangernews.utils.extension.coverToDateTime
import com.example.strangernews.utils.extension.loadByUrl

class HomeViewPagerAdapter(private val listener: ItemClickListener<Article>) :
    ListAdapter<Article, HomeViewPagerAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutHomeViewpagerItemBinding.inflate(LayoutInflater.from(parent.context))
        binding.root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                item = this
                binding.apply {
                    imageArticleViewPager.loadByUrl(image)
                    txtImagePagerTitle.text = title
                    txtImagePagerTime.text  = publishedAt.coverToDateTime()
                    imageButtonMore.setOnClickListener {
                        listener.onItemLongClick(item)
                    }
                }
            }
        }
    }

    class ViewHolder(
        val binding: LayoutHomeViewpagerItemBinding,
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
