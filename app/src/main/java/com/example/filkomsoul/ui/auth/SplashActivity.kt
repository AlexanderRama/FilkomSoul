package com.example.filkomsoul.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.filkomsoul.databinding.ActivitySplashBinding
import com.example.filkomsoul.ui.ViewModelFactory

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private var isLogin = true
    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.getAll().observe(this) {
            isLogin = it.isEmpty()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if(!isLogin) {
                val intent = Intent(this@SplashActivity, RegistActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@SplashActivity, OnboardActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 2500)
    }
}