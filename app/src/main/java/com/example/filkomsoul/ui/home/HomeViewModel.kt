package com.example.filkomsoul.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filkomsoul.data.UserRepository
import com.example.filkomsoul.data.local.MahasiswaEntity
import kotlinx.coroutines.launch

class HomeViewModel (private val userRepository: UserRepository) : ViewModel() {
    fun getAll() = userRepository.getResult()
}