package com.example.filkomsoul.ui.home.article

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filkomsoul.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val newsViewModel by viewModels<ArticleViewModel>()
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.back.setOnClickListener{
            finish()
        }
        articleAdapter = ArticleAdapter()
        binding.listarchive.adapter = articleAdapter
        val layoutManager = LinearLayoutManager(this)
        binding.listarchive.layoutManager = layoutManager
        binding.listarchive.layoutManager = GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false)

        newsViewModel.listNews.observe(this) { listnews ->
            articleAdapter.submitList(listnews)
        }
        newsViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}