package com.example.filkomsoul.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filkomsoul.data.UserRepository
import com.example.filkomsoul.ui.auth.AuthViewModel
import com.example.filkomsoul.ui.home.HomeViewModel
import com.example.filkomsoul.ui.profile.ProfileViewModel
import com.example.filkomsoul.ui.profile.archive.ArchiveViewModel
import com.example.filkomsoul.ui.screening.result.ResultViewModel

class ViewModelFactory private constructor(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(userRepository) as T
        }
        else if (modelClass.isAssignableFrom(ArchiveViewModel::class.java)) {
            return ArchiveViewModel(userRepository) as T
        }
        else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository) as T
        }
        else if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userRepository) as T
        }
        else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(UserRepository.getInstance(context))
            }.also { instance = it }
    }
}