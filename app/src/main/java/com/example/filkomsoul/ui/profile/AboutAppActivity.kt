package com.example.filkomsoul.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.filkomsoul.databinding.ActivityAboutAppBinding

class AboutAppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.back.setOnClickListener{
            finish()
        }
    }
}