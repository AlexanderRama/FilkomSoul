package com.example.filkomsoul.ui.screening.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filkomsoul.data.UserRepository
import com.example.filkomsoul.data.local.ArchiveEntity
import kotlinx.coroutines.launch

class ResultViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun setResult(result : ArchiveEntity) {
        viewModelScope.launch {
            userRepository.insertResult(result)
        }
    }

    fun getAll() = userRepository.getAllData()
}