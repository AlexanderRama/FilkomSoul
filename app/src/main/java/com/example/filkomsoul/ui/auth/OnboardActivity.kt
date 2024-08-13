package com.example.filkomsoul.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.filkomsoul.R
import com.example.filkomsoul.databinding.ActivityOnboardBinding
import kotlin.system.exitProcess

class OnboardActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOnboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnMulai.setOnClickListener{
            val intent = Intent(this@OnboardActivity, RegistActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.exit))
            setPositiveButton("Yes") { _, _ ->
                finish()
                exitProcess(0)
            }
            setNegativeButton("No", null)
        }.show()
        return true
    }
}