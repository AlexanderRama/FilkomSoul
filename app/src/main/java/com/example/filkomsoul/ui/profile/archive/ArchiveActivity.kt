package com.example.filkomsoul.ui.profile.archive

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filkomsoul.databinding.ActivityArchiveBinding
import com.example.filkomsoul.ui.ViewModelFactory

class ArchiveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArchiveBinding
    private lateinit var viewModel: ArchiveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val factory = ViewModelFactory.getInstance(this)
        val userAdapter = ArchiveAdapter()
        binding.listarchive.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
        }
        viewModel = ViewModelProvider(this, factory)[ArchiveViewModel::class.java]
        viewModel.getResult().observe(this) {
            userAdapter.submitList(it)
        }

        binding.back.setOnClickListener{
            finish()
        }
    }
}