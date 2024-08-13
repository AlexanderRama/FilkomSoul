package com.example.filkomsoul.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filkomsoul.data.UserRepository
import com.example.filkomsoul.data.local.ArchiveEntity
import com.example.filkomsoul.data.local.MahasiswaEntity
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun setMahasiswa(mahasiswa : MahasiswaEntity) {
        viewModelScope.launch {
            userRepository.insertMahasiswa(mahasiswa)
        }
    }

    fun setResult(archiveEntity: ArchiveEntity ) {
        viewModelScope.launch {
            userRepository.insertResult(archiveEntity)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            userRepository.delete()
        }
    }
    fun getAll() = userRepository.getAllData()
}