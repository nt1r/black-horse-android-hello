package com.example.black_horse_onboarding.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.black_horse_onboarding.R
import com.example.black_horse_onboarding.view_model.ArticleItem

class ArticleAdapter(private val dataset: MutableList<ArticleItem>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(articleView: View) : RecyclerView.ViewHolder(articleView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view: View = when (viewType) {
            ArticleItem.TYPE_ITEM -> {
                LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_layout, parent, false)
            }

            ArticleItem.TYPE_HEADER -> {
                LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_header_layout, parent, false)
            }

            else -> {
                LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_layout, parent, false)
            }
        }
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article: ArticleItem = dataset[position]

        if (article.type == ArticleItem.TYPE_ITEM) {
            val titleTextView = holder.itemView.findViewById<TextView>(R.id.list_item_title)
            val idTextView = holder.itemView.findViewById<TextView>(R.id.list_item_id)
            val descriptionTextView = holder.itemView.findViewById<TextView>(R.id.list_item_description)

            titleTextView.text = article.title
            idTextView.text = article.id.toString()
            descriptionTextView.text = article.description
        }
    }

    override fun getItemCount(): Int {
         return dataset.size
    }

    override fun getItemViewType(position: Int): Int {
        return dataset[position].type
    }
}