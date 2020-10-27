package com.example.black_horse_onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.black_horse_onboarding.adapters.ArticleAdapter
import com.example.black_horse_onboarding.view_model.ArticleItem

class ListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_layout)

        viewAdapter = ArticleAdapter(initArticleArray())
        viewManager = LinearLayoutManager(this)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun initArticleArray(): MutableList<ArticleItem> {
        val articles: MutableList<ArticleItem> = mutableListOf()
        for (index: Int in 1..20) {
            val article: ArticleItem = if (index == 1) {
                ArticleItem(ArticleItem.TYPE_HEADER, "Title ${index - 1}", index - 1, "Description ${index - 1}")
            } else {
                ArticleItem(ArticleItem.TYPE_ITEM, "Title ${index - 1}", index - 1, "Description ${index - 1}")
            }
            articles.add(article)
        }
        return articles
    }
}