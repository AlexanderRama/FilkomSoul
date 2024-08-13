package com.example.filkomsoul.ui.profile.archive

import androidx.lifecycle.ViewModel
import com.example.filkomsoul.data.UserRepository

class ArchiveViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getResult() = userRepository.getResult()
}