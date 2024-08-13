package com.example.filkomsoul.ui.profile

import androidx.lifecycle.ViewModel
import com.example.filkomsoul.data.UserRepository

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getMahasiswa() = userRepository.getAllData()
}